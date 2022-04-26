package ru.javawebinar.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "votes", uniqueConstraints =
@UniqueConstraint(columnNames = {"user_id", "voting_date"}))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "restaurant_id")
    private Integer restId;

    @Column(name = "voting_date")
    private LocalDate localDate;

    public Vote(Integer userId, Integer restaurantId) {
        this.userId = userId;
        this.restId = restaurantId;
        this.localDate = LocalDate.now();
    }

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
