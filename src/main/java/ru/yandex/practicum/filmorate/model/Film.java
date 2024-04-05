package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.related.ConstraintReleaseDate;
import ru.yandex.practicum.filmorate.related.FilmGenre;
import ru.yandex.practicum.filmorate.related.FilmRating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Builder
@Data
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotBlank
    ArrayList<FilmGenre> genre;
    @NotBlank
    FilmRating rating;
    @ConstraintReleaseDate
    LocalDate releaseDate;
    @PositiveOrZero
    Integer duration;
    Set<Integer> likes;
}
