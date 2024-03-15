package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private HashMap<Integer, Film> filmMap = new HashMap<>();
    int id = 1;


    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        validation(film);
        film.setId(id);
        filmMap.put(id, film);
        id++;
        return film;
    }


    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        validation(film);
        filmMap.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmList = new ArrayList<>(filmMap.values());
        return filmList;
    }


    private void validation(Film film) throws ValidationException {
        String message;
        if (film.getName().isBlank()) {
            message = "название не может быть пустым";
        } else if (film.getDescription().length() > 200) {
            message = "максимальная длина описания — 200 символов";
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            message = "дата релиза — не раньше 28 декабря 1895 года";
        } else if (film.getDuration().toMinutes() < 0) {
            message = "продолжительность фильма должна быть положительной";
        } else {
            return;
        }
        throw new ValidationException(message);
    }

}
