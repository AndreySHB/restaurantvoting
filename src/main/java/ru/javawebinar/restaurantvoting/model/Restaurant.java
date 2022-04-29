package ru.javawebinar.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Table(name = "restaurants", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "lunch_date"}, name = "unique_name_lunch_date_idx"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {
    @NotNull
    @Column(name = "lunch_date")
    private LocalDate lunchDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Meal> menu;

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", lunchDate=" + lunchDate +
                '}';
    }
}