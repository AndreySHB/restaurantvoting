package ru.javawebinar.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.javawebinar.restaurantvoting.model.Vote;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class VoteUtil {

    public static final LocalTime REFERENCE_TIME = LocalTime.of(11, 0);

    public static LocalTime BOUNDARY_TIME = REFERENCE_TIME;

    public static void setBoundaryTime(LocalTime lt) {
        BOUNDARY_TIME = lt;
    }

    public static int getWinner(List<Vote> votes) {
        Map<Integer, Long> voteMap = votes.stream().collect(Collectors.groupingBy(Vote::getRestId, Collectors.counting()));
        return votes.isEmpty() ? 0 : Collections.max(voteMap.entrySet(), (o1, o2) -> Math.toIntExact(o1.getValue() - o2.getValue())).getKey();
    }
}
