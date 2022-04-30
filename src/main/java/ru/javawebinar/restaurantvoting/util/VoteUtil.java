package ru.javawebinar.restaurantvoting.util;

import lombok.experimental.UtilityClass;

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

    public static int getWinner(List<Integer> votes) {
        Map<Integer, Long> voteMap = getVoteMap(votes);
        boolean isValid = !votes.isEmpty();
        if (!isValid) {
            return -1;
        }
        isValid = !voteMap.values().stream().allMatch(voteMap.get(votes.get(0))::equals);
        return isValid ? Collections.max(voteMap.entrySet(), (o1, o2) -> Math.toIntExact(o1.getValue() - o2.getValue())).getKey() : 0;
    }

    public static Map<Integer, Long> getVoteMap(List<Integer> votes) {
        return votes.stream().collect(Collectors.groupingBy(integer -> integer, Collectors.counting()));
    }
}
