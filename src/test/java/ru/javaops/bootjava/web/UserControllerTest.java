package ru.javaops.bootjava.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.UserTestUtil;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestUtil.userHttpBasic;
import static ru.javaops.bootjava.UserTestUtil.*;
import static ru.javaops.bootjava.util.JsonUtil.writeValue;

class UserControllerTest extends AbstractControllerTest {
    static final String URL = "/api/users/";

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonMatcher(user, UserTestUtil::assertNoIdEquals));
    }

    @Test
    void getAll() throws Exception {
        // TODO check content yourself
        perform(MockMvcRequestBuilders.get(URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "search/by-email?email=" + ADMIN_MAIL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonMatcher(admin, UserTestUtil::assertNoIdEquals));
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + USER_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.findById(USER_ID).isPresent());
        Assertions.assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    void create() throws Exception {
        User newUser = UserTestUtil.getNew();
        perform(MockMvcRequestBuilders.post(URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonMatcher(newUser, UserTestUtil::assertNoIdEquals));
    }

    @Test
    void update() throws Exception {
        User updated = UserTestUtil.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + USER_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());
        UserTestUtil.assertEquals(updated, userRepository.findById(USER_ID).orElseThrow());
    }
}