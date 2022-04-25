package ru.javawebinar.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Query;
import ru.javawebinar.restaurantvoting.model.Vote;

import java.util.List;

@Tag(name = "Vote Controller")
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v from Vote v WHERE v.localDate=:date")
    List<Vote> getAllByLocalDate(String date);

    @Query("SELECT v from Vote v")
    List<Vote> getAll();
}
