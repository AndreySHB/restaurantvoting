package ru.javawebinar.restaurantvoting.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.restaurantvoting.error.IllegalRequestDataException;
import ru.javawebinar.restaurantvoting.model.Vote;
import ru.javawebinar.restaurantvoting.repository.VoteRepository;
import ru.javawebinar.restaurantvoting.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/api";
    public static final LocalTime BOUNDARY_TIME = LocalTime.of(11, 0);

    @Autowired
    private VoteRepository repository;

    @GetMapping("/votes/current")
    public List<Vote> getAllCurrentDate() {
        log.info("getAllCurrentDate");
        return repository.getAllByLocalDate(LocalDate.now().toString());
    }

    @GetMapping("/votes/by-date")
    public List<Vote> getAllByDate(@RequestParam String date) {
        log.info("getAllByDate");
        return repository.getAllByLocalDate(date);
    }

    @GetMapping("/votes")
    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    //TODO update
    @PostMapping("/vote/{restId}")
    public void vote(@PathVariable int restId) {
        checkBoundaryTime();
        int userId = SecurityUtil.authId();
        log.info("voting {}", userId);
        repository.save(new Vote(userId, restId));
    }

    private void checkBoundaryTime() {
        if (LocalTime.now().isBefore(BOUNDARY_TIME)) {
            throw new IllegalRequestDataException("voting time is over");
        }
    }
}
