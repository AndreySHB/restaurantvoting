package ru.javawebinar.restaurantvoting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javawebinar.restaurantvoting.HasId;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class BaseTo implements HasId {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    protected Integer id;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}