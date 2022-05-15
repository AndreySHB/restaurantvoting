package ru.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantvoting.error.AppException;
import ru.restaurantvoting.model.Vote;
import ru.restaurantvoting.repository.VoteRepository;
import ru.restaurantvoting.util.VoteUtil;
import ru.restaurantvoting.util.validation.ValidationUtil;
import ru.restaurantvoting.web.SecurityUtil;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String USER_VOTES = "/api/votes/";
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

    @GetMapping(USER_VOTES)
    public List<Vote> getAllUserVotes() {
        log.info("getAllUserVotes");
        int authId = SecurityUtil.authId();
        return repository.getAllUserVotes(authId);
    }

    @GetMapping(USER_VOTES + "{id}")
    public Vote getUserVoteForToday() {
        log.info("getUserVote");
        int authId = SecurityUtil.authId();
        return repository.getByUserIdDate(authId, LocalDate.now());
    }

    @PostMapping(value = USER_VOTES, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@Valid @RequestBody Vote vote) {
        ValidationUtil.checkNew(vote);
        int authId = SecurityUtil.authId();
        vote.setUserId(authId);
        vote.setLocalDate(LocalDate.now());
        log.info("user {} voting  {}", authId, vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_VOTES).build().toUri();
        Vote created = repository.save(vote);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping(value = USER_VOTES, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void revote(@RequestParam @Positive int restId) {
        int authId = SecurityUtil.authId();
        LocalTime now = LocalTime.now();
        if (now.isBefore(VoteUtil.BOUNDARY_TIME)) {
            repository.update(authId, restId, LocalDate.now());
        } else {
            throw new AppException(HttpStatus.CONFLICT, "To late to change vote", ErrorAttributeOptions.defaults());
        }
    }
}
