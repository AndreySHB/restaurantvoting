package ru.javawebinar.restaurantvoting.web.meal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    public static final String USER_MEALS = "/api/meals";
    public static final String ADMIN_MEALS = "/api/admin/meals";
}
