package ru.javawebinar.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ru.javawebinar.restaurantvoting.model.Restaurant;
import ru.javawebinar.restaurantvoting.repository.MealRepository;
import ru.javawebinar.restaurantvoting.repository.RestaurantRepository;
import ru.javawebinar.restaurantvoting.util.JsonUtil;
import ru.javawebinar.restaurantvoting.util.VoteUtil;
import ru.javawebinar.restaurantvoting.web.AbstractControllerTest;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.restaurantvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.restaurantvoting.web.meal.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.restaurantvoting.web.meal.MealTestData.rest1Meals;
import static ru.javawebinar.restaurantvoting.web.restaurant.RestaurantController.ADMIN_RESTAURANTS;
import static ru.javawebinar.restaurantvoting.web.restaurant.RestaurantController.USER_RESTAURANTS;
import static ru.javawebinar.restaurantvoting.web.restaurant.RestaurantTestData.*;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.admin;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.user;
import static ru.javawebinar.restaurantvoting.web.vote.VoteTestData.YESTERDAY_DATE;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MealRepository mealRepository;

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS + "by-date")
                .param("date", YESTERDAY_DATE.toString())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(yesterdayRests));
    }

    @Test
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "for-today")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(todayRests));
    }

    @Test
    void getAllForTomorrow() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "for-tomorrow")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tomorrowRests));
    }

    @Test
    void getAllForVotingIsToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "for-voting")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(todayRests));
    }

    @Test
    void getAllForVotingIsTomorrow() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "for-voting")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tomorrowRests));
    }

    @Test
    void getById() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "1")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(rest1));

        Restaurant restaurant = RESTAURANT_MATCHER.readFromJson(actions);
        MEAL_MATCHER.assertMatch(restaurant.getMenu(), rest1Meals);
    }

    @Test
    void updateNameById() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS + "1")
                .param("name", "newName")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        Assertions.assertEquals("newName", restaurantRepository.getById(1).getName());
    }

    @Test
    void updateByIdBadName() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS + "1")
                        .param("name", "")
                        .with(userHttpBasic(admin))));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANTS + "1")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(restaurantRepository.getById(1));
        Assertions.assertTrue(mealRepository.getAllByRestId(1).isEmpty());
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restNew)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository
                .getByNameDate(restNew.getName(), restNew.getLunchDate()), restNew);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restNewInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }
}
