package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

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

    List<Genre> getGenres();

    Genre getGenre(Integer id);

    List<MPA> getMpa();

    MPA getMpas(Integer id);
}
