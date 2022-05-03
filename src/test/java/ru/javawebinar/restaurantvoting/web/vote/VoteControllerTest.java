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
import static ru.javawebinar.restaurantvoting.web.vote.VoteController.ADMIN_VOTES;
import static ru.javawebinar.restaurantvoting.web.vote.VoteController.USER_VOTE;
import static ru.javawebinar.restaurantvoting.web.vote.VoteTestData.*;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    VoteRepository voteRepository;

    @AfterEach
    public void deletePropagatedVote() {
        voteRepository.delete(NUM_VOTES_INBASE + 1);
    }

    @Test
    void getForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "for-today")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votes));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "by-date")
                .with(userHttpBasic(admin))
                .param("date", YESTERDAY_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votesOld));
    }

    @Test
    void getAll() throws Exception {
        ArrayList<Vote> votesAll = new ArrayList<>(votesOld);
        votesAll.addAll(votes);
        votesAll.addAll(List.of(adminVoteTomorrow));

        perform(MockMvcRequestBuilders.get(ADMIN_VOTES)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votesAll));
    }

    @Test
    void getWinnerForToday() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "winner-by-date")
                .param("date", LocalDate.now().toString())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(EXPECTED_WINNER_ID, actualWinnerId);
    }

    @Test
    void getWinnerEmptyVotes() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "winner-by-date")
                .param("date", LocalDate.MAX.toString())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(EMPTY_VOTES_CODE, actualWinnerId);
    }

    @Test
    void getWinnerAllEquals() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "winner-by-date")
                .param("date", YESTERDAY_DATE.toString())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(NO_WINNER_CODE, actualWinnerId);
    }

    @Test
    void getVoteMap() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(ADMIN_VOTES + "votemap-by-date")
                .param("date", YESTERDAY_DATE.toString())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        String content = getContentAsString(actions);
        Assertions.assertEquals(JSON_VOTE_MAP_OLD, content);
    }

    @Test
    void voteForToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(USER_VOTE + "4")
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votesWithNew);
    }

    @Test
    void voteForTomorrow() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.post(USER_VOTE + "7")
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        List<Vote> tomorrowVotes = voteRepository.getAllByLocalDate(TOMORROW_DATE);
        VOTE_MATCHER.assertMatch(tomorrowVotes, userVoteTomorrow, adminVoteTomorrow);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void revoteForToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(USER_VOTE + "5")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        adminVote.setRestId(5);
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votes);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void revoteForTomorrow() throws Exception {
        perform(MockMvcRequestBuilders.post(USER_VOTE + "7")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        adminVoteTomorrow.setRestId(7);
        List<Vote> tomorrowVotes = voteRepository.getAllByLocalDate(TOMORROW_DATE);
        VOTE_MATCHER.assertMatch(tomorrowVotes, adminVoteTomorrow);
    }

    @Test
    void voteUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(USER_VOTE + "4"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void voteForNotPresentRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.post(USER_VOTE + "1")
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }
}
