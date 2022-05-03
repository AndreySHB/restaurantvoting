package ru.javawebinar.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.model.Meal;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.id=:id")
    Meal getById(int id);

    @Transactional
    @Modifying
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Query("DELETE FROM Meal m WHERE m.id=:id")
    int delete(int id);

    @Query("SELECT m FROM Meal m WHERE m.restId=:restId ORDER BY m.id")
    List<Meal> getAllByRestId(int restId);

    @Transactional
    @Modifying
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Query("UPDATE Meal m SET m.name=:name, m.price=:price WHERE m.id=:id")
    void update(String name, double price, int id);

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Override
    <S extends Meal> S save(S entity);
}
