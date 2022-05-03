package ru.javawebinar.restaurantvoting.web.meal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.restaurantvoting.error.IllegalRequestDataException;
import ru.javawebinar.restaurantvoting.model.Meal;
import ru.javawebinar.restaurantvoting.repository.MealRepository;
import ru.javawebinar.restaurantvoting.repository.RestaurantRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    public static final String ADMIN_MEAL = "/api/admin/meal/";
    public static final String ADMIN_MEALS = "/api/admin/meals/";

    @Autowired
    MealRepository mealRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping(ADMIN_MEALS + "{restId}")
    List<Meal> getAllByRestId(@PathVariable int restId) {
        log.info("getAllMealsByRestId {}", restId);
        return mealRepository.getAllByRestId(restId);
    }

    @GetMapping(ADMIN_MEAL + "{id}")
    Meal getById(@PathVariable int id) {
        log.info("getMealById {}", id);
        return mealRepository.getById(id);
    }

    @DeleteMapping(ADMIN_MEAL + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable int id) {
        log.info("deleteMealById {}", id);
        mealRepository.delete(id);
    }

    @PostMapping(ADMIN_MEAL + "update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateById(@Valid @RequestBody Meal meal, @PathVariable int id) {
        log.info("updateMealById {}", id);
        mealRepository.update(meal.getName(), meal.getPrice(), id);
    }

    @PostMapping(ADMIN_MEAL + "{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void save(@Valid @RequestBody Meal meal, @PathVariable int restId) {
        checkRestIdExists(restId);
        log.info("saveNewMealForRestId {}", restId);
        meal.setRestId(restId);
        mealRepository.save(meal);
    }

    private void checkRestIdExists(int restId) {
        if (restaurantRepository.getById(restId) == null) {
            throw new IllegalRequestDataException("Restaurant with id=" + restId + " not found");
        }
    }
}
