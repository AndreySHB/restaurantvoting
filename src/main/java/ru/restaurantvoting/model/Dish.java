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
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "dish", uniqueConstraints =
@UniqueConstraint(columnNames = {"rest_id", "lunch_date", "name"}, name = "unique_restid_date_name_idx"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {
    @Positive
    @NotNull
    @Column(name = "price", nullable = false)
    private int price;

    @NotNull
    @Column(name = "rest_id", nullable = false)
    private Integer restId;

    @NotNull
    @Column(name = "lunch_date", nullable = false)
    private LocalDate lunchDate;

    public Dish(String name, int price) {
        super(null, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}