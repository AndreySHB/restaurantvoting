package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Restaurant Controller")
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant getById(int id);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.dishes" +
            " d WHERE d.lunchDate=:localDate ORDER BY r.name")
    List<Restaurant> getAllWithMenuForDate(LocalDate localDate);
}
