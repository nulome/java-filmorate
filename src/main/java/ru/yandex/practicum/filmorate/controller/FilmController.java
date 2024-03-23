package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
        log.info("Получен запрос Post /films - {}", film.getName());
        film.setId(id);
        filmMap.put(id, film);
        id++;
        return film;
    }


    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос Put /films - {}", film.getName());
        if (!filmMap.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный id фильма");
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос Get /films");
        List<Film> filmList = new ArrayList<>(filmMap.values());
        return filmList;
    }


}
