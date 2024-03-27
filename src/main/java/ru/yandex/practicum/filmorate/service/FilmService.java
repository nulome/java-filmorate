package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Set<Integer> addLikes(Integer filmId, Integer userId);

    Set<Integer> deleteLikes(Integer filmId, Integer userId);

    List<Film> getPopularMovies(Integer count);

    Film getFilm(Integer id);
}
