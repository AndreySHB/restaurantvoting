package ru.restaurantvoting.web.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    public static final String ADMIN_DISH = "/api/admin/dish/";
    public static final String ADMIN_DISHES = "/api/admin/dishes/";

    @Autowired
    DishRepository repository;

    @GetMapping(ADMIN_DISHES + "{restId}")
    List<Dish> getAllByRestId(@PathVariable int restId) {
        log.info("getAllMealsByRestId {}", restId);
        return repository.getAllByRestId(restId);
    }

    @GetMapping(ADMIN_DISH + "{id}")
    Dish getById(@PathVariable int id) {
        log.info("getMealById {}", id);
        return repository.getById(id);
    }

    @DeleteMapping(ADMIN_DISH + "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable int id) {
        log.info("deleteMealById {}", id);
        repository.delete(id);
    }

    @PutMapping(ADMIN_DISH + "update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        ValidationUtil.checkNew(dish);
        log.info("updating dish {}", id);
        dish.setId(id);
        repository.save(dish);
    }

    @PostMapping(ADMIN_DISH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        log.info("creating new dish {}", dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_DISH).build().toUri();
        Dish created = repository.save(dish);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
