package ru.javawebinar.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.restaurantvoting.error.IllegalRequestDataException;
import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.repository.VoteRepository;
import ru.javawebinar.restaurantvoting.util.VoteUtil;
import ru.javawebinar.restaurantvoting.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api";
    public static final LocalTime BOUNDARY_TIME = LocalTime.of(23, 0);

    @Autowired
    private VoteRepository repository;

    @GetMapping("/votes/today")
    public List<Vote> getAllCurrentDate() {
        log.info("getAllCurrentDate");
        return repository.getAllByLocalDate(LocalDate.now());
    }

    @GetMapping("/votes/by-date")
    public List<Vote> getAllByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate");
        return repository.getAllByLocalDate(date);
    }

    @GetMapping("/votes")
    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/votes/winner-for-today")
    public int getWinner() {
        log.info("getWinner");
        return VoteUtil
                .getWinner(repository.getAllByLocalDate(LocalDate.now()));
    }

    @PostMapping("/vote/{restId}")
    public void vote(@PathVariable int restId) {
        checkBoundaryTime();
        int userId = SecurityUtil.authId();
        log.info("user {} voting", userId);
        try {
            repository.save(new Vote(userId, restId));
        } catch (DataIntegrityViolationException e) {
            repository.update(userId, restId, LocalDate.now());
        }
    }

    private void checkBoundaryTime() {
        if (LocalTime.now().isAfter(BOUNDARY_TIME)) {
            throw new IllegalRequestDataException("Voting time is over");
        }
    }
}
