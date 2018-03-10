package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.List;


import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        get(id,userId);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal=repository.get(id);
        checkNotFoundWithId(meal, id);
        checkNotFound(meal.getUserId()==userId,"user want to get a food which how does not belong to him");
        return meal;
    }

    @Override
    public void update(Meal meal, int userId) {
        checkNotFound(meal.getUserId()==userId,"user want to change a food which how does not belong to him");
        repository.save(meal);
    }

    @Override
    public List<Meal> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Meal> getAllByUserId(int id) {
        return repository.getAllByUserId(id);
    }
}