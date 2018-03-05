package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

public interface CrudForMap extends GenericDao<Meal> {
    Meal findById(int id);
}
