package ru.javawebinar.topjava.daoimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.SimpleDB;
import ru.javawebinar.topjava.dao.CrudForMap;
import ru.javawebinar.topjava.model.Meal;
import java.util.ArrayList;
import java.util.List;



public class ImplCDUDFromJava implements CrudForMap {
    private static final Logger LOG = LoggerFactory.getLogger(ImplCDUDFromJava.class);

    @Override
    public List<Meal> selectAll() {
        LOG.debug("get from crud");
        return new ArrayList<>(SimpleDB.getMeals().values());
    }

    @Override
    public void create(Meal meal) {
        SimpleDB.addInMap(meal);
    }

    @Override
    public void update(Meal meal) {
        SimpleDB.updateMap(meal.getId(),meal);
    }

    @Override
    public void delete(int id) {
        SimpleDB.deleteMap(id);
    }


    public Meal findById(int id) {
        return SimpleDB.getMeals().get(id);
    }
}
