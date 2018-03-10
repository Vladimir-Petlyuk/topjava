package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AbstractUserController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;
    private ProfileRestController userController;
    private User user;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        userController = appCtx.getBean(ProfileRestController.class);
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ( action!=null && action.equals("sorted")) {
            String firstDate=request.getParameter("firstDate");
            String endDate = request.getParameter("endDate");
            String firstTime = request.getParameter("firstTime");
            String endTime = request.getParameter("endTime");


            List<MealWithExceed> list;
            if (!firstDate.isEmpty() && !endDate.isEmpty() && !firstTime.isEmpty() && !endTime.isEmpty()) {
                LocalDateTime first = LocalDateTime.of(LocalDate.parse(firstDate), LocalTime.parse(firstTime));
                LocalDateTime end = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));
                list = mealRestController.sortedByDateTime(first, end, user.getId());
                request.setAttribute("meals", list);
            } else if (!firstTime.isEmpty() && !endTime.isEmpty()) {
                list = mealRestController.sortedByTime(LocalTime.parse(firstTime), LocalTime.parse(endTime), user.getId());
                request.setAttribute("meals", list);
            } else if (!firstDate.isEmpty() && !endDate.isEmpty()) {
                list = mealRestController.sortedByDate(LocalDate.parse(firstDate), LocalDate.parse(endDate), user.getId());
                request.setAttribute("meals", list);
            } else {
                response.sendRedirect("meals");
                return;
            }
            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        } else {

            String id = request.getParameter("id");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")), user.getId());

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                mealRestController.create(meal);
            } else {
                mealRestController.update(meal, user.getId());
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        user = userController.get();
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id, user.getId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, user.getId()) :
                        mealRestController.get(getId(request), user.getId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        user.getRoles().contains(Role.ROLE_ADMIN) ? mealRestController.getAll() : mealRestController.getAllByid(user.getId()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }
}
