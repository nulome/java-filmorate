package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceLogic;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService filmServiceLogic;

    @Autowired
    public FilmController(FilmServiceLogic filmServiceLogic) {
        this.filmServiceLogic = filmServiceLogic;
    }


    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmServiceLogic.createFilm(film);
    }


    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmServiceLogic.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmServiceLogic.getFilms();
    }

}
