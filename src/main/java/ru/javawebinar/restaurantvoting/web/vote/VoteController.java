package ru.javawebinar.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    public static final String ADMIN_VOTES = "/admin/votes";

    @Autowired
    private VoteRepository repository;

    @GetMapping(ADMIN_VOTES + "/for-today")
    public List<Vote> getForToday() {
        log.info("getAllCurrentDate");
        return repository.getAllByLocalDate(LocalDate.now());
    }

    @GetMapping(ADMIN_VOTES + "/by-date")
    public List<Vote> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate");
        return repository.getAllByLocalDate(date);
    }

    @GetMapping(ADMIN_VOTES)
    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping(ADMIN_VOTES + "/winner-for-today")
    public int getWinnerForToday() {
        log.info("getWinner-for-today");
        return VoteUtil
                .getWinner(repository.getAllByLocalDate(LocalDate.now()));
    }

    @PostMapping("/vote/{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int restId) {
        LocalDate ld = LocalTime.now().isBefore(VoteUtil.BOUNDARY_TIME) ?
                LocalDate.now() : LocalDate.now().plusDays(1);
        int userId = SecurityUtil.authId();
        log.info("user {} voting for restaurant {} on {} date", userId, restId, ld);
        try {
            repository.save(new Vote(userId, restId, ld));
        } catch (DataIntegrityViolationException e) {
            repository.update(userId, restId, ld);
        }
    }
}
