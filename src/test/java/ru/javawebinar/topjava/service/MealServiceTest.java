package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration(
        "classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Before
    public void setUp() throws Exception {
      repository= MealTestData.repository;
    }

    @Test
    public void get() {
        Meal expectMeal = service.get(START_SEQ,USER_ID);
        Meal actualMeal = repository
                .get(USER_ID)
                .get(START_SEQ);
        assertMatch(actualMeal, expectMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        Meal expectMeal = service.get(START_SEQ,ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(START_SEQ,USER_ID);
        assertThat(service.getAll(USER_ID).size()).isEqualTo(repository.get(USER_ID).values().size()-1);

    }
    @Test(expected = NotFoundException.class)
    public void notFoundDelete() {
        service.delete(START_SEQ,ADMIN_ID);

    }


    @Test
    public void getAll() {
        Collection<Meal> actualMeals=repository
                .get(USER_ID).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        Collection<Meal> expectMeals=service.getAll(USER_ID);

        assertMatch(actualMeals,expectMeals);
    }

    @Test
    public void update() {
        Meal meal = service.get(START_SEQ, USER_ID);
        meal.setDescription("Brekfast");
        meal.setCalories(5000);
        service.update(meal,USER_ID);
        assertMatch(service.get(START_SEQ,USER_ID),meal);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundUpdate() {
        Meal meal = service.get(START_SEQ, USER_ID);
        meal.setDescription("Brekfast");
        meal.setCalories(5000);
        service.update(meal,ADMIN_ID);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,30,13,00),"Завтрак",2000);
        assertMatch(meal,service.create(meal, ADMIN_ID));
    }
    @Test(expected = DataAccessException.class)
    public void notFoundCreate() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY,30,13,00),"Завтрак",2000);
        service.create(meal, USER_ID);
    }
}