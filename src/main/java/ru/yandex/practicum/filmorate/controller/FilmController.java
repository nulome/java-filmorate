package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос Post /films - {}", film.getName());
        return inMemoryFilmStorage.createFilm(film);
    }


    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос Put /films - {}", film.getName());
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос Get /films");
        return inMemoryFilmStorage.getFilms();
    }

}
