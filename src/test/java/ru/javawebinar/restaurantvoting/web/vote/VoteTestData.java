package ru.javawebinar.restaurantvoting.web.vote;

import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "id");
    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static final LocalDate OLD_DATE = LocalDate.of(2020, 2, 1);
    public static final LocalDate TOMORROW_DATE = LocalDate.now().plusDays(1);

    public static final int EXPECTED_WINNER_ID = 100;

    public static final Vote adminVote = new Vote(2, 100, CURRENT_DATE);
    public static final Vote mashaVote = new Vote(3, 300, CURRENT_DATE);
    public static final Vote dashaVote = new Vote(4, 100, CURRENT_DATE);
    public static final Vote sashaVote = new Vote(5, 200, CURRENT_DATE);
    public static final Vote pashaVote = new Vote(6, 100, CURRENT_DATE);

    public static final Vote userVote = new Vote(1, 100, CURRENT_DATE);
    public static final Vote userVoteTomorrow = new Vote(1, 100, TOMORROW_DATE);
    public static final Vote adminVoteTomorrow = new Vote(2, 300, TOMORROW_DATE);

    public static final List<Vote> votes = List.of(adminVote, mashaVote, dashaVote, sashaVote, pashaVote);
    public static final List<Vote> votesWithNew = List.of(adminVote, mashaVote, dashaVote, sashaVote, pashaVote, userVote);

    public static final Vote adminVoteOld = new Vote(2, 100, OLD_DATE);
    public static final Vote mashaVoteOld = new Vote(3, 300, OLD_DATE);
    public static final Vote dashaVoteOld = new Vote(4, 100, OLD_DATE);
    public static final Vote sashaVoteOld = new Vote(5, 200, OLD_DATE);
    public static final Vote pashaVoteOld = new Vote(6, 100, OLD_DATE);

    public static final List<Vote> votesOld = List.of(adminVoteOld, mashaVoteOld, dashaVoteOld, sashaVoteOld, pashaVoteOld);
}
