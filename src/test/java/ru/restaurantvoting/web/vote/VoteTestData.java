package ru.restaurantvoting.web.vote;

import ru.restaurantvoting.model.Vote;
import ru.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "id");

    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static final LocalDate YESTERDAY_DATE = LocalDate.now().minusDays(1);
    public static final LocalDate TOMORROW_DATE = LocalDate.now().plusDays(1);

    public static final int NUM_VOTES_INBASE = 12;

    public static final int EXPECTED_WINNER_ID = 4;

    public static final Vote adminVote = new Vote(2, 4, CURRENT_DATE);
    public static final Vote mashaVote = new Vote(3, 6, CURRENT_DATE);
    public static final Vote dashaVote = new Vote(4, 4, CURRENT_DATE);
    public static final Vote sashaVote = new Vote(5, 5, CURRENT_DATE);
    public static final Vote pashaVote = new Vote(6, 4, CURRENT_DATE);

    public static final Vote userVoteAdded = new Vote(1, 4, CURRENT_DATE);
    public static final Vote userVoteTomorrow = new Vote(1, 7, TOMORROW_DATE);
    public static final Vote adminVoteTomorrow = new Vote(2, 8, TOMORROW_DATE);

    public static final List<Vote> votes = List.of(adminVote, mashaVote, dashaVote, sashaVote, pashaVote);
    public static final List<Vote> votesWithNew = List.of(userVoteAdded, adminVote, mashaVote, dashaVote, sashaVote, pashaVote);

    public static final Vote userVoteOld = new Vote(1, 1, YESTERDAY_DATE);
    public static final Vote adminVoteOld = new Vote(2, 1, YESTERDAY_DATE);
    public static final Vote mashaVoteOld = new Vote(3, 2, YESTERDAY_DATE);
    public static final Vote dashaVoteOld = new Vote(4, 2, YESTERDAY_DATE);
    public static final Vote sashaVoteOld = new Vote(5, 3, YESTERDAY_DATE);
    public static final Vote pashaVoteOld = new Vote(6, 3, YESTERDAY_DATE);

    public static final List<Vote> votesOld = List.of(userVoteOld, adminVoteOld, mashaVoteOld, dashaVoteOld, sashaVoteOld, pashaVoteOld);

    public static final int EMPTY_VOTES_CODE = -1;
    public static final int NO_WINNER_CODE = 0;
    public static final String JSON_VOTE_MAP_OLD = "{\"1\":2,\"2\":2,\"3\":2}";
}
