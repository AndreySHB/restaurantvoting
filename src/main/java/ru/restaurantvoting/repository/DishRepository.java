package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Dish Controller")
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id")
    Dish getById(int id);

    @Transactional
    @Modifying
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(int id);

    @Query("SELECT d FROM Dish d WHERE d.restId=:restId ORDER BY d.id")
    List<Dish> getAllByRestId(int restId);

    @Transactional
    @Modifying
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Query("UPDATE Dish d SET d.name=:name, d.price=:price WHERE d.id=:id")
    void update(String name, double price, int id);

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Override
    <S extends Dish> S save(S entity);

    @Query("SELECT d FROM Dish d WHERE d.lunchDate=:date")
    List<Dish> getAllByDate(LocalDate date);
}
