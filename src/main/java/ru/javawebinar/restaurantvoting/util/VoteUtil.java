package ru.javawebinar.restaurantvoting.util;

import ru.javawebinar.restaurantvoting.model.Vote;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteUtil {

    public static int getWinner(List<Vote> votes) {
        Map<Integer, Long> voteMap = votes.stream().collect(Collectors.groupingBy(Vote::getRestId, Collectors.counting()));
        return votes.isEmpty() ? 0 : Collections.max(voteMap.entrySet(), (o1, o2) -> Math.toIntExact(o1.getValue() - o2.getValue())).getKey();
    }
}
