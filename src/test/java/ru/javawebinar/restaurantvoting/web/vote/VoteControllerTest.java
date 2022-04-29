package ru.javawebinar.restaurantvoting.web.vote;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.repository.VoteRepository;
import ru.javawebinar.restaurantvoting.util.VoteUtil;
import ru.javawebinar.restaurantvoting.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.restaurantvoting.TestUtil.userHttpBasic;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.admin;
import static ru.javawebinar.restaurantvoting.web.user.UserTestData.user;
import static ru.javawebinar.restaurantvoting.web.vote.VoteTestData.*;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_ADMIN_VOTES_URL = "/api/admin/votes/";
    private static final String REST_USER_VOTE_URL = "/api/vote/";

    @Autowired
    VoteRepository voteRepository;

    @AfterEach //strange datapropagation behaviour
    public void deletePropagatedVote(){
        voteRepository.delete(INIT_VOTES_INBASE +1);
    }

    @Test
    void getForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_VOTES_URL + "for-today/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votes));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_ADMIN_VOTES_URL + "by-date/")
                .with(userHttpBasic(admin))
                .param("date", OLD_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votesOld));
    }

    @Test
    void getAll() throws Exception {
        ArrayList<Vote> votesAll = new ArrayList<>(votesOld);
        votesAll.addAll(votes);
        perform(MockMvcRequestBuilders.get(REST_ADMIN_VOTES_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votesAll));
    }

    @Test
    void getWinnerForToday() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(REST_ADMIN_VOTES_URL + "winner-for-today/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(actions.andReturn().getResponse().getContentAsString());
        Assertions.assertEquals(EXPECTED_WINNER_ID, actualWinnerId);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void voteForToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/100/")
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votesWithNew);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void voteForTomorrow() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/100/")
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        List<Vote> tomorrowVotes = voteRepository.getAllByLocalDate(TOMORROW_DATE);
        VOTE_MATCHER.assertMatch(tomorrowVotes, userVoteTomorrow);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void revoteForToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/200/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        adminVote.setRestId(200);
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votes);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void revoteForTomorrow() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/300/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        List<Vote> tomorrowVotes = voteRepository.getAllByLocalDate(TOMORROW_DATE);
        VOTE_MATCHER.assertMatch(tomorrowVotes, adminVoteTomorrow);

        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/200/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        adminVoteTomorrow.setRestId(200);
        tomorrowVotes = voteRepository.getAllByLocalDate(TOMORROW_DATE);
        VOTE_MATCHER.assertMatch(tomorrowVotes, adminVoteTomorrow);
    }

    @Test
    void voteUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_USER_VOTE_URL + "/100/"))
                .andExpect(status().isUnauthorized());
    }
}
