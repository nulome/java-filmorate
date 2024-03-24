package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();
    Set<Integer> addLikes(Integer idFilm, Integer idUser);
    Set<Integer> deleteLikes(Integer idFilm, Integer idUser);
    List<Film> getPopularMovies(Integer count);
}
