package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getAllByid(int id) {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAllByUserId(id), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> sortedByTime(LocalTime first, LocalTime end, int id) {
        log.info("sortedByTime");
        return MealsUtil.getFilteredWithExceededByTime(service.getAllByUserId(id), MealsUtil.DEFAULT_CALORIES_PER_DAY,first,end);
    }

    public List<MealWithExceed> sortedByDate(LocalDate first, LocalDate end, int id) {
        log.info("sortedByDate");
        return MealsUtil.getFilteredWithExceededByDate(service.getAllByUserId(id), MealsUtil.DEFAULT_CALORIES_PER_DAY,first,end);
    }

    public List<MealWithExceed> sortedByDateTime(LocalDateTime first, LocalDateTime end, int id) {
        log.info("sortedByDate");
        return MealsUtil.getFilteredWithExceededByDateTime(service.getAllByUserId(id), MealsUtil.DEFAULT_CALORIES_PER_DAY,first,end);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id, int userId) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} with id={}", meal, meal.getId());
        service.update(meal, userId);
    }

    public Meal get(int id, int userId) {
        log.info("get {} with id={}", id);
        return service.get(id, userId);
    }

}