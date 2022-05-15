package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.model.Restaurant;

import java.util.List;

@Tag(name = "Restaurant Controller")
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Cacheable("restaurants")
    @Override
    List<Restaurant> findAll();

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
}
