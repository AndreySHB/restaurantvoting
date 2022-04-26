package ru.javawebinar.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Vote Controller")
@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Modifying
    @Query("SELECT v FROM Vote v WHERE v.localDate=:date")
    List<Vote> getAllByLocalDate(LocalDate date);

    @Query("SELECT v FROM Vote v")
    List<Vote> getAll();

    @Modifying
    @Transactional
    @Query("UPDATE Vote v SET v.restId = :restId WHERE v.localDate=:localDate AND v.userId=:userId")
    void update(int userId, int restId, LocalDate localDate);
}
