package ru.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.model.Restaurant;
import ru.restaurantvoting.repository.RestaurantRepository;
import ru.restaurantvoting.util.JsonUtil;
import ru.restaurantvoting.web.AbstractControllerTest;
import ru.restaurantvoting.web.user.UserTestData;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantvoting.web.dish.DishTestData.*;
import static ru.restaurantvoting.web.restaurant.RestaurantController.ADMIN_RESTAURANTS;
import static ru.restaurantvoting.web.restaurant.RestaurantController.USER_RESTAURANTS;
import static ru.restaurantvoting.web.restaurant.RestaurantTestData.*;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(ALL_RESTS));
    }


    @Test
    void getWithMenuForToday() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(USER_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        String contentAsString = getContentAsString(actions);
        List<Restaurant> restaurants = JsonUtil.readValues(contentAsString, Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(restaurants,RESTS_WITH_MENU_FOR_TODAY);

        DISH_MATCHER.assertMatch(restaurants.get(0).getDishes(),List.of(D3,D4));
        DISH_MATCHER.assertMatch(restaurants.get(1).getDishes(),List.of(D1,D2));


    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(REST1));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(restaurantRepository.getById(1));
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NEW_REST)))
                .andExpect(status().isCreated());

        NEW_REST.setId(4);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(4), NEW_REST);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(ADMIN_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(UPDATED_REST1)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(1), UPDATED_REST1);
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NEW_REST_INVALIDNAME)))
                .andExpect(status().isUnprocessableEntity());
    }
}
