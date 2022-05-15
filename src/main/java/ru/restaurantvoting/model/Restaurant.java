package ru.restaurantvoting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "restaurant")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> menu;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes;

    public Restaurant(String name) {
        super(null, name);
    }

    public Restaurant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}