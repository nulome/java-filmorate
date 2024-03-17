package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.related.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final HashMap<Integer, Film> filmMap = new HashMap<>();
    int id = 1;


    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос Post /users");
        film.setId(id);
        filmMap.put(id, film);
        id++;
        return film;
    }


    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос Put /users");
        if(!filmMap.containsKey(film.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный id фильма");
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmList = new ArrayList<>(filmMap.values());
        return filmList;
    }


    /*private void validation(Film film) throws ValidationException {
        String message;
        if (film.getName().isBlank()) {
            message = "название не может быть пустым";
        } else if (film.getDescription().length() > 200) {
            message = "максимальная длина описания — 200 символов";
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            message = "дата релиза — не раньше 28 декабря 1895 года";
        } else if (film.getDuration() < 0) {
            message = "продолжительность фильма должна быть положительной";
        } else {
            return;
        }
        log.warn(message);
        throw new ValidationException(message);
    }*/

}
