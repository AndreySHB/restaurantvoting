package ru.javawebinar.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.TestUtil;
import ru.javawebinar.restaurantvoting.model.Role;
import ru.javawebinar.restaurantvoting.model.User;
import ru.javawebinar.restaurantvoting.repository.UserRepository;
import ru.javawebinar.restaurantvoting.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.restaurantvoting.web.user.AdminUserController.ADMIN_USERS;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.*;


class AdminUserControllerTest extends AbstractControllerTest {

    private static final String ADMIN_USERS_URL = ADMIN_USERS + '/';

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + ADMIN_ID)
                .with(TestUtil.userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + NOT_FOUND)
                .with(TestUtil.userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL + "by-email?email=" + admin.getEmail())
                .with(TestUtil.userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS_URL + USER_ID)
                .with(TestUtil.userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(USER_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS_URL + NOT_FOUND)
                .with(TestUtil.userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL)
                .with(TestUtil.userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(ADMIN_USERS_URL + USER_ID)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "newPass")))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getById(USER_ID), getUpdated());
    }

    @Test
    void create() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_USERS_URL)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS_URL)
                .with(TestUtil.userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, dasha, masha, pasha, sasha, user));
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(ADMIN_USERS_URL)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        User invalid = new User(user);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(ADMIN_USERS_URL + USER_ID)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(user);
        updated.setEmail(ADMIN_MAIL);
        perform(MockMvcRequestBuilders.put(ADMIN_USERS_URL + USER_ID)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = new User(null, "New", USER_MAIL, "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(ADMIN_USERS_URL)
                .with(TestUtil.userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}