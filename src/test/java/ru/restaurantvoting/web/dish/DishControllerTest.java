package ru.restaurantvoting.web.dish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.repository.DishRepository;
import ru.restaurantvoting.util.JsonUtil;
import ru.restaurantvoting.web.AbstractControllerTest;
import ru.restaurantvoting.web.user.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository repository;

    @Test
    void getAllByRestId() throws Exception {
        perform(MockMvcRequestBuilders.get(DishController.ADMIN_DISHES + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.REST_1_DISHES));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(DishController.ADMIN_DISH + "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.DISH_2_REST_1));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(DishController.ADMIN_DISH + "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(repository.getAllByRestId(1), DishTestData.DISH_1_REST_1);
    }

    @Test
    void updateById() throws Exception {
        perform(MockMvcRequestBuilders.post(DishController.ADMIN_DISH + "update/1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.DISH_1_REST_1_UPDATED)))
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(repository.getById(1), DishTestData.DISH_1_REST_1_UPDATED);
    }

    @Test
    void updateByIdBadPrice() throws Exception {
        perform(MockMvcRequestBuilders.post(DishController.ADMIN_DISH + "update/1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.DISH_1_REST_1_UPDATED_BAD_PRICE)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void save() throws Exception {
        perform(MockMvcRequestBuilders.post(DishController.ADMIN_DISH + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.DISH_NEW_REST_1)))
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(repository.getAllByRestId(1), DishTestData.REST_1_MEALS_NEW);
    }

    @Test
    void saveBadRestId() throws Exception {
        perform(MockMvcRequestBuilders.post(DishController.ADMIN_DISH + "1000")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.DISH_NEW_REST_1)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void saveDuplicatedNames() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.post(DishController.ADMIN_DISH + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.DISH_NEW_REST_1_DUPLICATED_NAMES))));
    }
}
