package ru.javawebinar.restaurantvoting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String USER_RESTAURANTS = "/api/restaurants";
    public static final String ADMIN_RESTAURANTS = "/api/admin/restaurants";
}
