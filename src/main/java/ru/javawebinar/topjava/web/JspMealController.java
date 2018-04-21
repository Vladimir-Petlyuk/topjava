package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.Util.orElse;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
   @Autowired
   private MealService mealService;

    @GetMapping({"", "/all"})
    public String meals(Model model) {
        int userId=AuthorizedUser.id();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", MealsUtil.getWithExceeded(mealService.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }


    @GetMapping("/create")
    public String crateMeal(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String updateMeal(HttpServletRequest request, Model model) {
        int userId=AuthorizedUser.id();
        int id = getId(request);
        model.addAttribute("meal", mealService.get(id, userId));
        return "mealForm";
    }

    @GetMapping("/delete")
    public String deleteMeal(HttpServletRequest request) {
        int userId=AuthorizedUser.id();
        int id = getId(request);
        mealService.delete(id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }


    @PostMapping("/filter")
    public String filterMeal(HttpServletRequest request, Model model) {
        int userId=AuthorizedUser.id();
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<Meal> mealsDateFiltered = mealService.getBetweenDates(
                orElse(startDate, DateTimeUtil.MIN_DATE), orElse(endDate, DateTimeUtil.MAX_DATE), userId);

        List<MealWithExceed> list= MealsUtil.getFilteredWithExceeded(mealsDateFiltered, AuthorizedUser.getCaloriesPerDay(),
                orElse(startTime, LocalTime.MIN), orElse(endTime, LocalTime.MAX)
        );

        model.addAttribute("meals", list);
        return "meals";
    }

    @PostMapping("/edit")
    public String editMeal(HttpServletRequest request) {
        int userId=AuthorizedUser.id();
        Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
                checkNew(meal);
                mealService.create(meal, userId);
            } else {
                int id = getId(request);
                assureIdConsistent(meal, id);
                mealService.update(meal, userId);
            }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
