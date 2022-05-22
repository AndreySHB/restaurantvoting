package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Transactional
    @Modifying
    @Query("UPDATE Dish d SET d.name=:name, d.price=:price WHERE d.id=:id")
    void update(String name, double price, int id);

    @Query("SELECT d FROM Dish d WHERE d.restId=:restId AND d.lunchDate=:lunchDate ORDER BY d.name")
    List<Dish> getAllByRestIdDate(int restId, LocalDate lunchDate);

    @Query("SELECT d FROM Dish d WHERE d.id=:id")
    Dish getById(int id);
}
