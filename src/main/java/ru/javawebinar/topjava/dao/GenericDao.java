package ru.javawebinar.topjava.dao;

import java.util.List;

public interface GenericDao<T> {
    List<T> selectAll();
    void create(T t);
    void update(T t);
    void delete(int id);
}
