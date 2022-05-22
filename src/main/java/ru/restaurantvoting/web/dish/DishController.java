package ru.restaurantvoting.web.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantvoting.model.Dish;
import ru.restaurantvoting.repository.DishRepository;
import ru.restaurantvoting.util.validation.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    public static final String ADMIN_DISHES = "/api/admin/dishes/";
    public static final String USER_DISHES = "/api/dishes/";

    @Autowired
    DishRepository repository;

    @GetMapping(ADMIN_DISHES + "{id}")
    public Dish getById(@PathVariable int id) {
        log.info("getMealById {}", id);
        return repository.getById(id);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @DeleteMapping(ADMIN_DISHES + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("deleteMealById {}", id);
        repository.deleteById(id);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @PutMapping(ADMIN_DISHES + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        ValidationUtil.assureIdConsistent(dish, id);
        log.info("updating dish {}", id);
        repository.save(dish);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @PostMapping(ADMIN_DISHES)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        ValidationUtil.checkNew(dish);
        log.info("creating new dish {}", dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_DISHES).build().toUri();
        Dish created = repository.save(dish);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Cacheable("dishes")
    @GetMapping(USER_DISHES)
    public List<Dish> getDishesForRestaurantForToday(@RequestParam int restId) {
        log.info("getRestaurantMenu {}", restId);
        return repository.getAllByRestIdDate(restId, LocalDate.now());
    }
}
