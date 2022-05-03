package ru.javawebinar.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.restaurantvoting.error.AppException;
import ru.javawebinar.restaurantvoting.model.BaseEntity;
import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.repository.RestaurantRepository;
import ru.javawebinar.restaurantvoting.repository.VoteRepository;
import ru.javawebinar.restaurantvoting.util.VoteUtil;
import ru.javawebinar.restaurantvoting.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String USER_VOTE = "/api/vote/";
    public static final String ADMIN_VOTES = "/api/admin/votes/";

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping(ADMIN_VOTES + "for-today")
    public List<Vote> getForToday() {
        log.info("getAllCurrentDate");
        return voteRepository.getAllByLocalDate(LocalDate.now());
    }

    @GetMapping(ADMIN_VOTES + "by-date")
    public List<Vote> getByDate(@RequestParam
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate");
        return voteRepository.getAllByLocalDate(date);
    }

    @GetMapping(ADMIN_VOTES)
    public List<Vote> getAll() {
        log.info("getAll");
        return voteRepository.getAll();
    }

    @GetMapping(ADMIN_VOTES + "winner-by-date")
    public int getWinnerByDate(@RequestParam
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWinner-at-{}", date);
        return VoteUtil.getWinner(voteRepository.getAllOnlyIdsByLocalDate(date));
    }

    @GetMapping(ADMIN_VOTES + "votemap-by-date")
    public Map<Integer, Long> getVoteMapByDate(@RequestParam
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getVoteMap-at-{}", date);
        return VoteUtil.getVoteMap(voteRepository.getAllOnlyIdsByLocalDate(date));
    }

    @PostMapping(USER_VOTE + "{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int restId) {
        LocalDate ld = LocalTime.now().isBefore(VoteUtil.BOUNDARY_TIME) ?
                LocalDate.now() : LocalDate.now().plusDays(1);
        checkRestIdExistsByDate(restId, ld);
        int userId = SecurityUtil.authId();
        log.info("user {} voting for restaurant {} on {} date", userId, restId, ld);
        try {
            voteRepository.save(new Vote(userId, restId, ld));
        } catch (DataIntegrityViolationException e) {
            voteRepository.update(userId, restId, ld);
        }
    }

    private void checkRestIdExistsByDate(int restId, LocalDate date) {
        if (restaurantRepository.getRestByDate(date).stream()
                .map(BaseEntity::id).noneMatch(integer -> integer == restId)) {
            throw new AppException(HttpStatus.FORBIDDEN,
                    String.format("No Restaurant with id %d for date %s", restId, date), ErrorAttributeOptions.defaults());
        }
    }
}
