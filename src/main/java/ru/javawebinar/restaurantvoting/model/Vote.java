package ru.javawebinar.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "votes", uniqueConstraints =
@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "unique_user_vote_date_idx"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Range(min = 1)
    @Column(name = "user_id")
    private Integer userId;

    @Range(min = 1)
    @Column(name = "restaurant_id")
    private Integer restId;

    @NotNull
    @Column(name = "vote_date")
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
