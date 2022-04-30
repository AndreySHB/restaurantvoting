package ru.javawebinar.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Tag(name = "Restaurant Controller")
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    //    @Cacheable
    @Query("SELECT r.id FROM Restaurant r WHERE r.lunchDate=:date")
    List<Integer> getRestIdsByDate(LocalDate date);
    //    @Cacheable
    @Query("SELECT r FROM Restaurant r WHERE r.lunchDate=:date")
    List<Restaurant> getRestByDate(LocalDate date);
}
