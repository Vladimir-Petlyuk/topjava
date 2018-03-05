package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleDB {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleDB.class);
    private static AtomicInteger id = new AtomicInteger(0);
    private static int id_;
    private static ConcurrentMap<Integer, Meal> meals = new ConcurrentHashMap<>();
    static {
        LOG.debug("init");
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 1, 10, 0), "Завтрак", 1000));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 1, 13, 0), "Обед", 500));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 1, 20, 0), "Ужин", 510));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 2, 10, 0), "Завтрак", 500));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 2, 13, 0), "Обед", 1000));
        meals.put(modifyAIInInt(id),new Meal(id_,LocalDateTime.of(2015, Month.JUNE, 2, 20, 0), "Ужин", 500));
    }

    public static ConcurrentMap<Integer, Meal> getMeals() {
        LOG.debug("get meals");
        return meals;
    }

    private static Integer modifyAIInInt(AtomicInteger id){
        id_=Integer.parseInt(String.valueOf(id.incrementAndGet()));
        return id_;
    }

    public static void addInMap(Meal meal){
        meals.put(modifyAIInInt(id),new Meal(id_,meal.getDateTime(),meal.getDescription(),meal.getCalories()));
    }

    public static void updateMap(int id, Meal meal){
        meals.put(id,meal);
    }
    public static void deleteMap(int id){
        meals.remove(id);
    }
}
