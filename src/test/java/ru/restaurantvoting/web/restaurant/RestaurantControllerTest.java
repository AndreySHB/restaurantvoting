package ru.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.repository.DishRepository;
import ru.restaurantvoting.repository.RestaurantRepository;
import ru.restaurantvoting.util.JsonUtil;
import ru.restaurantvoting.web.AbstractControllerTest;
import ru.restaurantvoting.web.user.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantvoting.web.restaurant.RestaurantTestData.*;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;


    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(ALL_RESTS));
    }


    @Test
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(ALL_RESTS));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(REST1));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantController.ADMIN_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(restaurantRepository.getById(1));
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NEW_REST)))
                .andExpect(status().isCreated());

        NEW_REST.setId(4);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(4), NEW_REST);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(RestaurantController.ADMIN_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(UPDATED_REST1)))
                .andExpect(status().isNoContent());

//        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getById(1), REST1); TODO
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NEW_REST_INVALID)))
                .andExpect(status().isUnprocessableEntity());
    }
}
