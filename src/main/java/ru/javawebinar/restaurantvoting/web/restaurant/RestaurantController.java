package ru.javawebinar.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.restaurantvoting.model.Restaurant;
import ru.javawebinar.restaurantvoting.repository.RestaurantRepository;
import ru.javawebinar.restaurantvoting.util.VoteUtil;
import ru.javawebinar.restaurantvoting.util.validation.ValidationUtil;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @GetMapping(USER_RESTAURANTS + "for-today")
    List<Restaurant> getAllForToday() {
        log.info("getAllRestaurants for today");
        return repository.getRestByDate(LocalDate.now());
    }

    @GetMapping(USER_RESTAURANTS + "for-tomorrow")
    List<Restaurant> getAllForTomorrow() {
        log.info("getAllRestaurants for tomorrow");
        return repository.getRestByDate(LocalDate.now().plusDays(1));
    }

    @GetMapping(USER_RESTAURANTS + "for-voting")
    List<Restaurant> getAvailibleForVoting() {
        log.info("getAllRestaurantsAvailible for voting");
        LocalDate ld = LocalTime.now().isBefore(VoteUtil.BOUNDARY_TIME) ?
                LocalDate.now() : LocalDate.now().plusDays(1);
        return repository.getRestByDate(ld);
    }

    @GetMapping(ADMIN_RESTAURANTS + "by-date")
    List<Restaurant> getAllByDate(@RequestParam
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllRestaurants by date");
        return repository.getRestByDate(date);
    }

    @GetMapping(USER_RESTAURANTS + "{id}")
    Restaurant get(@PathVariable int id) {
        log.info("getRestaurantbyId {}", id);
        return repository.getById(id);
    }

    //only name can be updated
    @PostMapping(ADMIN_RESTAURANTS + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable int id, @RequestParam @Size(min = 2, max = 128) String name) {
        log.info("updateRestaurantById {} new name {}", id, name);
        repository.update(id, name);
    }

    @PostMapping(value = ADMIN_RESTAURANTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void save(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        log.info("saveNewRestaurant {}", restaurant);
        repository.save(restaurant);
    }

    @DeleteMapping(ADMIN_RESTAURANTS + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        repository.delete(id);
    }
}
