package ru.javawebinar.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Tag(name = "Restaurant Controller")
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Cacheable("restaurants")
    @Query("SELECT r FROM Restaurant r WHERE r.lunchDate=:date ORDER BY r.id")
    List<Restaurant> getRestByDate(LocalDate date);

    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.name = :name WHERE r.id=:id")
    void update(int id, String name);

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Override
    <S extends Restaurant> S save(S entity);

    @Transactional
    @CacheEvict(value = {"restaurants", "restaurant"}, allEntries = true)
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(int id);

    //for users to inspect restaurant
    @Cacheable("restaurant")
    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant getById(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.name=:name AND r.lunchDate=:date")
    Restaurant getByNameDate(String name, LocalDate date);
}
