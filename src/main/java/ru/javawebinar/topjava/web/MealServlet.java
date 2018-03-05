package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.CrudForMap;
import ru.javawebinar.topjava.daoimpl.ImplCDUDFromJava;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;


public class MealServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd MMM yyyy");
    private static final LocalTime MIN = LocalTime.MIN;
    private static final LocalTime MAX = LocalTime.MAX;
    private CrudForMap crud= new  ImplCDUDFromJava();
    private List<MealWithExceed> listMeals = initList(2000);
    private final String LIST_MEALS = "/meals.jsp";
    private final String INSERT_OR_EDIT = "/editMeal.jsp";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("doGet meals");

        String forward;
        String action = request.getParameter("action");
        if (action == null) {
            forward = LIST_MEALS;
            request.setAttribute("listMeals", listMeals);

        } else {

            if (action.equalsIgnoreCase("delete")) {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                crud.delete(mealId);
                forward ="/topjava/meals";
                listMeals=initList(2000);
                response.sendRedirect(response.encodeRedirectURL(forward));
                return;

            } else if (action.equalsIgnoreCase("edit")) {
                forward = INSERT_OR_EDIT;
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = crud.findById(mealId);
                request.setAttribute("meal", meal);

            } else if (action.equalsIgnoreCase("meals")) {
                forward = LIST_MEALS;
                request.setAttribute("listMeals", listMeals);


            }
            else{
                forward = INSERT_OR_EDIT;
            }
        }
        request.setAttribute("formatter", formatter);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("doPost meals");
        request.setCharacterEncoding("cp1251");

        LocalDateTime date = LocalDateTime.parse(request.getParameter("doc"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        if (request.getParameter("mealId").equals(""))
            crud.create(new Meal(0,date,description,calories));
        else {
        int id=Integer.parseInt(request.getParameter("mealId"));
            crud.update(new Meal(id,date,description,calories));
        }

        listMeals=initList(2000);
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("formatter", formatter);
        request.setAttribute("listMeals", listMeals);
        view.forward(request, response);
    }

    private List<MealWithExceed> initList(int calories){
        List<MealWithExceed> list = MealsUtil.getFilteredWithExceededInOnePass2(crud.selectAll(), MIN, MAX, calories);
        list.sort(Comparator.comparing(MealWithExceed::getDateTime));
        return list;
    }
}
