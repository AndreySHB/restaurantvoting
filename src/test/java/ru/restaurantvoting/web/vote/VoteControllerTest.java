package ru.restaurantvoting.web.vote;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;
import ru.restaurantvoting.TestUtil;
import ru.restaurantvoting.model.Vote;
import ru.restaurantvoting.repository.VoteRepository;
import ru.restaurantvoting.util.JsonUtil;
import ru.restaurantvoting.util.VoteUtil;
import ru.restaurantvoting.web.AbstractControllerTest;
import ru.restaurantvoting.web.user.UserTestData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurantvoting.web.vote.VoteTestData.*;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    VoteRepository voteRepository;

    @Test
    void getForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "for-today")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votes));
    }

    @Test
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "by-date")
                .with(TestUtil.userHttpBasic(UserTestData.admin))
                .param("date", YESTERDAY_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(votesOld));
    }

    @Test
    void getWinnerForToday() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "winner-by-date")
                .param("date", LocalDate.now().toString())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(EXPECTED_WINNER_ID, actualWinnerId);
    }

    @Test
    void getWinnerEmptyVotes() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "winner-by-date")
                .param("date", LocalDate.MAX.toString())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(EMPTY_VOTES_CODE, actualWinnerId);
    }

    @Test
    void getWinnerAllEquals() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "winner-by-date")
                .param("date", YESTERDAY_DATE.toString())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Integer actualWinnerId = Integer.valueOf(getContentAsString(actions));
        Assertions.assertEquals(NO_WINNER_CODE, actualWinnerId);
    }

    @Test
    void getVoteMap() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.get(VoteController.ADMIN_VOTES + "votemap-by-date")
                .param("date", YESTERDAY_DATE.toString())
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        String content = getContentAsString(actions);
        Assertions.assertEquals(JSON_VOTE_MAP_OLD, content);
    }

    @Test
    void voteForToday() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(VoteController.USER_VOTES)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.user))
                .content(JsonUtil.writeValue(newUserVote)))
                .andExpect(status().isCreated());
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votesWithNew);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void revote() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.patch(VoteController.USER_VOTES)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restId", "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isOk());
        adminVote.setRestId(2);
        List<Vote> todayVotes = voteRepository.getAllByLocalDate(LocalDate.now());
        VOTE_MATCHER.assertMatch(todayVotes, votes);
    }

    @Test
    void revoteToLate() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.patch(VoteController.USER_VOTES)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restId", "2")
                .with(TestUtil.userHttpBasic(UserTestData.admin)))
                .andExpect(status().isConflict());
    }

    @Test
    void voteUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.post(VoteController.USER_VOTES))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void revoteNotPresent() throws Exception {
        VoteUtil.setBoundaryTime(LocalTime.MAX);
        Assertions.assertThrows(NestedServletException.class, () ->
                perform(MockMvcRequestBuilders.patch(VoteController.USER_VOTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("restId", "20")
                        .with(TestUtil.userHttpBasic(UserTestData.admin))).andExpect(status().isOk()));
    }
}
