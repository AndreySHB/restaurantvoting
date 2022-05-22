package ru.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantvoting.model.Restaurant;
import ru.restaurantvoting.repository.RestaurantRepository;
import ru.restaurantvoting.util.validation.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String USER_RESTAURANTS = "/api/restaurants/";
    public static final String ADMIN_RESTAURANTS = "/api/admin/restaurants/";

    @Autowired
    RestaurantRepository restaurantRepository;

    @Cacheable("restaurant")
    @GetMapping(USER_RESTAURANTS + "{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("getRestaurantWithDishes {}", id);
        return restaurantRepository.getById(id);
    }

    @Cacheable("restaurants_for_today")
    @Transactional
    @GetMapping(USER_RESTAURANTS)
    public List<Restaurant> getAllForToday() {
        log.info("getAllRestaurantsWithMenuForToday");
        return restaurantRepository.getAllWithMenuForDate(LocalDate.now())
                .stream().filter(r -> r.getDishes() != null).collect(toList());
    }

    @GetMapping(ADMIN_RESTAURANTS)
    public List<Restaurant> getAll() {
        log.info("getAllRestaurants");
        return restaurantRepository.findAll();
    }

    @CacheEvict(value = {"restaurants_for_today", "restaurant"}, allEntries = true)
    @PostMapping(value = ADMIN_RESTAURANTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        log.info("saveNewRestaurant {}", restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_RESTAURANTS).build().toUri();
        Restaurant created = restaurantRepository.save(restaurant);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @CacheEvict(value = {"restaurants_for_today", "restaurant"}, allEntries = true)
    @PutMapping(value = ADMIN_RESTAURANTS + "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.assureIdConsistent(restaurant, id);
        log.info("update resturant with id {}", id);
        restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"restaurants_for_today", "restaurant, dishes"}, allEntries = true)
    @DeleteMapping(ADMIN_RESTAURANTS + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        restaurantRepository.delete(id);
    }
}
