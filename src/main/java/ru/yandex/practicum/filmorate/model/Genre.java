package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class Genre implements Comparable<Genre> {
    @NotNull
    int id;
    String name;

    @Override
    public int compareTo(Genre o) {
        return this.id - o.id;
    }
}
