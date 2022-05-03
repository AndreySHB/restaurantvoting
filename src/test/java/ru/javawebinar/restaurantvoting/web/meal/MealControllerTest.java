package ru.javawebinar.restaurantvoting.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ru.javawebinar.restaurantvoting.repository.MealRepository;
import ru.javawebinar.restaurantvoting.util.JsonUtil;
import ru.javawebinar.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.restaurantvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.restaurantvoting.web.meal.MealController.ADMIN_MEAL;
import static ru.javawebinar.restaurantvoting.web.meal.MealController.ADMIN_MEALS;
import static ru.javawebinar.restaurantvoting.web.meal.MealTestData.*;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.admin;

public class MealControllerTest extends AbstractControllerTest {

    @Autowired
    MealRepository repository;

    @Test
    void getAllByRestId() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_MEALS + "1")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(MEAL_MATCHER.contentJson(rest1Meals));
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_MEAL + "2")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(MEAL_MATCHER.contentJson(meal2Rest1));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_MEAL + "2")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(repository.getAllByRestId(1), meal1Rest1);
    }

    @Test
    void updateById() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_MEAL + "update/1")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal1Rest1Updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(repository.getById(1), meal1Rest1Updated);
    }

    @Test
    void updateByIdBadPrice() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_MEAL + "update/1")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal1Rest1UpdatedBadPrice)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void save() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_MEAL + "1")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mealNewRest1)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(repository.getAllByRestId(1), rest1MealsNew);
    }

    @Test
    void saveBadRestId() throws Exception {
        perform(MockMvcRequestBuilders.post(ADMIN_MEAL + "1000")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mealNewRest1)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void saveDuplicatedNames() throws Exception {
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.post(ADMIN_MEAL + "1")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mealNewRest1DuplicatedNames))));
    }
}
