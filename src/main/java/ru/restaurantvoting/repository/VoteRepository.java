package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Vote Controller")
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.localDate=:date ORDER BY v.userId")
    List<Vote> getAllByLocalDate(LocalDate date);

    @Modifying
    @Transactional
    @Query("UPDATE Vote v SET v.restId = :restId WHERE  v.userId=:userId AND v.localDate=:localDate")
    void update(int userId, int restId, LocalDate localDate);

    @Query("SELECT v.restId FROM Vote v WHERE v.localDate=:date")
    List<Integer> getAllOnlyIdsByLocalDate(LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.userId = :userId AND v.localDate = :date")
    Vote getByUserIdDate(int userId, LocalDate dat);

    @Query("SELECT v FROM Vote v WHERE v.userId = :userId order by v.localDate DESC")
    List<Vote> getAllUserVotes(int userId);
}
