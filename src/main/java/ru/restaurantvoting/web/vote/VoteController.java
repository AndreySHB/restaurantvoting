package ru.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantvoting.model.Vote;
import ru.restaurantvoting.repository.VoteRepository;
import ru.restaurantvoting.util.VoteUtil;
import ru.restaurantvoting.web.SecurityUtil;

import java.net.URI;
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
    private VoteRepository repository;

    @GetMapping(ADMIN_VOTES + "for-today")
    public List<Vote> getForToday() {
        log.info("getAllCurrentDate");
        return repository.getAllByLocalDate(LocalDate.now());
    }

    @GetMapping(ADMIN_VOTES + "by-date")
    public List<Vote> getByDate(@RequestParam
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate");
        return repository.getAllByLocalDate(date);
    }

    @GetMapping(ADMIN_VOTES + "winner-by-date")
    public int getWinnerByDate(@RequestParam
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWinner-at-{}", date);
        return VoteUtil.getWinner(repository.getAllOnlyIdsByLocalDate(date));
    }

    @GetMapping(ADMIN_VOTES + "votemap-by-date")
    public Map<Integer, Long> getVoteMapByDate(@RequestParam
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getVoteMap-at-{}", date);
        return VoteUtil.getVoteMap(repository.getAllOnlyIdsByLocalDate(date));
    }

    @PostMapping(USER_VOTE + "{restId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable int restId) {
        LocalDate ld = LocalDate.now();
        int userId = SecurityUtil.authId();
        log.info("user {} voting for restaurant {} on {} date", userId, restId, ld);
        Vote existedVote = repository.getByDateUserID(ld, userId);
        Vote newVote = new Vote(userId, restId, ld);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_VOTE + "{restId}").build().toUri();
        if (existedVote == null) {
            return ResponseEntity.created(uriOfNewResource).body(repository.save(newVote));
        } else if (LocalTime.now().isBefore(VoteUtil.BOUNDARY_TIME)) {
            newVote.setId(existedVote.getId());
            return ResponseEntity.created(uriOfNewResource).body(repository.save(newVote));
        }
        return null;
    }
}
