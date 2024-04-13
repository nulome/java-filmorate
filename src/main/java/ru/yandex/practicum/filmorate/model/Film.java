package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.related.ConstraintReleaseDate;
import ru.yandex.practicum.filmorate.related.FilmGenre;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    Set<FilmGenre> genre;
    String rating;
    @ConstraintReleaseDate
    LocalDate releaseDate;
    @PositiveOrZero
    Integer duration;
    Set<Integer> likes;
}
