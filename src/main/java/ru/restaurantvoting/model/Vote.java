package ru.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "vote", uniqueConstraints =
@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "unique_user_vote_date_idx"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    @NotNull
    @Column(name = "rest_id", nullable = false)
    private Integer restId;

    @Column(name = "vote_date" , nullable = false, updatable = false)
    private LocalDate localDate;

    public Vote(Integer userId, Integer restId, LocalDate localDate) {
        this.userId = userId;
        this.restId = restId;
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "userId=" + userId +
                ", restId=" + restId +
                ", localDate=" + localDate +
                '}';
    }
}
