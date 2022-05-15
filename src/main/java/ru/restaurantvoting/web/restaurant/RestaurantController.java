package ru.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantvoting.model.Dish;
import ru.restaurantvoting.model.Restaurant;
import ru.restaurantvoting.repository.DishRepository;
import ru.restaurantvoting.repository.RestaurantRepository;
import ru.restaurantvoting.util.RestaurantUtil;
import ru.restaurantvoting.util.validation.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String USER_RESTAURANTS = "/api/restaurants/";
    public static final String ADMIN_RESTAURANTS = "/api/admin/restaurants/";

    @Autowired
    RestaurantRepository repository;

    @Autowired
    DishRepository dishRepository;

    @GetMapping(USER_RESTAURANTS + "{id}")
    Restaurant get(@PathVariable int id) {
        log.info("getRestaurantbyId {}", id);
        return repository.getById(id);
    }

    @GetMapping(ADMIN_RESTAURANTS)
    List<Restaurant> getAll() {
        log.info("getAllRestaurants");
        return repository.findAll();
    }

    @GetMapping(USER_RESTAURANTS)
    List<Restaurant> getAllWithMenuForToday() {
        log.info("getAllRestaurantsWithMenuForToday");
        List<Dish> dishesForToday = dishRepository.getAllByDate(LocalDate.now());
        return RestaurantUtil.getRestaurantsWithMenuForToday(dishesForToday, repository.findAll());
    }

    @PostMapping(value = ADMIN_RESTAURANTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        log.info("saveNewRestaurant {}", restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_RESTAURANTS).build().toUri();
        Restaurant created = repository.save(restaurant);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = ADMIN_RESTAURANTS + "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        log.info("saveNewRestaurant {}", restaurant);
        restaurant.setId(id);
        repository.save(restaurant);
    }

    @DeleteMapping(ADMIN_RESTAURANTS + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        repository.delete(id);
    }
}
