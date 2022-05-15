package ru.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.model.Role;
import ru.restaurantvoting.model.User;
import ru.restaurantvoting.repository.UserRepository;
import ru.restaurantvoting.web.AbstractControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantvoting.web.user.UserController.ADMIN_USERS;


class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS + UserTestData.ADMIN_ID)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS + UserTestData.NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS + "by-email?email=" + UserTestData.admin.getEmail())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.admin));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(UserTestData.USER_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_USERS + UserTestData.NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS)
                .with(TestUtil.userHttpBasic(UserTestData.user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = UserTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(ADMIN_USERS + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(updated, "newPass")))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserTestData.USER_MATCHER.assertMatch(userRepository.getById(UserTestData.USER_ID), UserTestData.getUpdated());
    }

    @Test
    void create() throws Exception {
        User newUser = UserTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_USERS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());

        User created = UserTestData.USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_USERS)
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.admin, UserTestData.dasha, UserTestData.masha, UserTestData.pasha, UserTestData.sasha, UserTestData.user));
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(ADMIN_USERS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        User invalid = new User(UserTestData.user);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(ADMIN_USERS + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(UserTestData.user);
        updated.setEmail(UserTestData.ADMIN_MAIL);
        perform(MockMvcRequestBuilders.put(ADMIN_USERS + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = new User(null, "New", UserTestData.USER_MAIL, "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(ADMIN_USERS)
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTestData.jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}