package ru.restaurantvoting.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.repository.DishRepository;
import ru.restaurantvoting.repository.RestaurantRepository;
import ru.restaurantvoting.util.VoteUtil;
import ru.restaurantvoting.web.AbstractControllerTest;
import ru.restaurantvoting.web.user.UserTestData;
import ru.restaurantvoting.web.vote.VoteTestData;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

    @Test
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.ADMIN_RESTAURANTS + "by-date")
                .param("date", VoteTestData.YESTERDAY_DATE.toString())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.yesterdayRests));*/
    }

    @Test
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "for-today")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.todayRests));*/
    }

    @Test
    void getAllForTomorrow() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "for-tomorrow")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.tomorrowRests));*/
    }

    @Test
    void getAllForVotingIsToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "for-voting")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.todayRests));*/
    }

    @Test
    void getAllForVotingIsTomorrow() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "for-voting")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.tomorrowRests));*/
    }

    @Test
    void getById() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(RestaurantController.USER_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
                /*.andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.rest1));*/

        /*Restaurant restaurant = RestaurantTestData.RESTAURANT_MATCHER.readFromJson(actions);
        DishTestData.DISH_MATCHER.assertMatch(restaurant.getMenu(), DishTestData.REST_1_DISHES);*/
    }

    @Test
    void updateNameById() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS + "1")
                .param("name", "newName")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());
        Assertions.assertEquals("newName", restaurantRepository.getById(1).getName());
    }

    @Test
    void updateByIdBadName() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS + "1")
                        .param("name", "")
                        .with(TestUtil.userHttpBasic(UserTestData.admin))));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(RestaurantController.ADMIN_RESTAURANTS + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(restaurantRepository.getById(1));
        Assertions.assertTrue(dishRepository.getAllByRestId(1).isEmpty());
    }

   /* @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RestaurantTestData.restNew)))
                .andExpect(status().isNoContent());

        *//*RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurantRepository
                .getByNameDate(RestaurantTestData.restNew.getName(), RestaurantTestData.restNew.getLunchDate()), RestaurantTestData.restNew);*//*
    }*/

   /* @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(RestaurantController.ADMIN_RESTAURANTS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RestaurantTestData.restNewInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }*/
}
