package ru.javawebinar.restaurantvoting.web.vote;

import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "id");
    public static final LocalDate VOTING_DATE = LocalDate.of(2020, 2, 1);
    public static final Vote userVote = new Vote(1, 100, VOTING_DATE);
    public static final Vote mashaVote = new Vote(3, 300, VOTING_DATE);
    public static final Vote dashaVote = new Vote(4, 100, VOTING_DATE);
    public static final Vote sashaVote = new Vote(5, 200, VOTING_DATE);
    public static final Vote pashaVote = new Vote(6, 100, VOTING_DATE);
}
