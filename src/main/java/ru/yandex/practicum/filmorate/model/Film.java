package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.related.ConstraintReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Data
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @ConstraintReleaseDate
    LocalDate releaseDate;
    @PositiveOrZero
    Integer duration;
}
