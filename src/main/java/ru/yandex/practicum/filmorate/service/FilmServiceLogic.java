package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.related.Constants;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceLogic implements FilmService {
    FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmServiceLogic(FilmStorage filmStorage) {
        this.inMemoryFilmStorage = filmStorage;
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос Post /films - {}", film.getName());
        film.setLikes(new HashSet<>());
        return inMemoryFilmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос Put /films - {}", film.getName());
        return inMemoryFilmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        log.trace("Получен запрос Get /films");
        return inMemoryFilmStorage.getFilms();
    }

    @Override
    public Set<Integer> addLikes(Integer filmId, Integer userId) {
        log.debug("Получен запрос PUT /films/{}/like/{} - лайк фильму", filmId, userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        film.getLikes().add(userId);
        inMemoryFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public Set<Integer> deleteLikes(Integer filmId, Integer userId) {
        log.debug("Получен запрос DELETE /users/{}/friends/{} - удаление лайка", filmId, userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        inMemoryFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public List<Film> getPopularMovies(Integer count) {
        log.trace("Получен запрос GET /films/popular?count={} - топ по лайкам", count);
        if(count==null){
            count= Constants.DEFAULT_POPULAR_VALUE;
        }
        List<Film> films = inMemoryFilmStorage.getFilms();
        return films.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

}
