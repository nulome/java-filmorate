package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmControllerTest {

    FilmController filmController;
    Film film;
    Validator validator;
/*

    @BeforeEach
    void create() {
        filmController = new FilmController();
        film = Film.builder()
                .name("nameFilm")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(80)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void checkTestMapping() {
        Film checkFilm = filmController.createFilm(film);
        assertEquals(1, checkFilm.getId(), "Не записывается id");

        checkFilm = filmController.createFilm(film);
        assertEquals(2, checkFilm.getId(), "Не обновляется id");

        film.setId(1);
        film.setName("UpdName");
        film.setDescription("Update");
        filmController.updateFilm(film);
        assertEquals(film, filmController.getFilms().get(1), "Не корректно обновляет film");

        assertEquals(2, filmController.getFilms().size(), "Не сохраняет фильмы");
    }

    @Test
    void checkValidationMethodFilmController() {
        film.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Пропустил пустое название");

        film.setName("nameFilm");
        film.setReleaseDate(LocalDate.of(1500, 1, 1));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Фильм с невозможной датой релиза");

        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-3);
        violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Продолжительность фильма должна быть положительной");
    }
*/

}