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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantvoting.web.dish.DishController.ADMIN_DISHES;
import static ru.restaurantvoting.web.dish.DishController.USER_DISHES;
import static ru.restaurantvoting.web.dish.DishTestData.*;

public class DishControllerTest extends AbstractControllerTest {

    @Autowired
    DishRepository repository;

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_DISHES + "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.DISH_2));
    }

    @Test
    void getMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_DISHES)
                .param("restId","2")
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isOk())
                .andExpect(DishTestData.DISH_MATCHER.contentJson(List.of(D3,D4)));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_DISHES + "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isNoContent());

        Assertions.assertNull(repository.getById(2));
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(ADMIN_DISHES + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NEW_DISH1)))
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(repository.getById(1), NEW_DISH1);
    }

    @Test
    void updateByIdBadPrice() throws Exception {
        perform(MockMvcRequestBuilders.put(ADMIN_DISHES + "1")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.NEW_DISH_BAD_PRICE)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void save() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_DISHES)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.NEW_DISH)))
                .andExpect(status().isCreated());

        DishTestData.DISH_MATCHER.assertMatch(repository.getById(17), DishTestData.NEW_DISH);
    }

    @Test
    void saveDuplicated() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.post(ADMIN_DISHES)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(DishTestData.NEW_DISH_DUPLICATED))).andExpect(status().isCreated()));
    }
}
