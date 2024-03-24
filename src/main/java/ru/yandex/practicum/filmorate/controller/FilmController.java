package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceLogic;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {
    final String DEFAULT_POPULAR_VALUE = "10";
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

    @GetMapping("/{filmId}")
    public Film getUser(@PathVariable int filmId) {
        return filmServiceLogic.getFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Set<Integer> addLikes(@PathVariable int filmId, int userId) {
        return filmServiceLogic.addLikes(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Set<Integer> deleteLikes(@PathVariable int filmId, int userId) {
        return filmServiceLogic.deleteLikes(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularMovies(@RequestParam(defaultValue = DEFAULT_POPULAR_VALUE) Integer count) {
        return filmServiceLogic.getPopularMovies(count);
    }

}
