package ru.javawebinar.restaurantvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.restaurantvoting.repository.VoteRepository;
import ru.javawebinar.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.restaurantvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.admin;
import static ru.javawebinar.restaurantvoting.web.vote.VoteTestData.*;

public class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/api/votes/";

    @Autowired
    VoteRepository voteRepository;

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date/")
                .with(userHttpBasic(admin))
                .param("date", VOTING_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote, mashaVote, dashaVote, sashaVote, pashaVote))
                .andDo(print());
    }
}
