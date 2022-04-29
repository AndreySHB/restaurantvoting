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

@Getter
@Setter
@Table(name = "meals", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "restaurant_id"}, name = "unique_name_restaurant_id_idx"))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal extends NamedEntity {
    @Range(min = 1)
    @Column(name = "price")
    private double price;

    @Override
    public String toString() {
        return "Meal{" +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}