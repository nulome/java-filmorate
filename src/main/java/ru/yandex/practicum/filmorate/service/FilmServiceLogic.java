package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.related.Constants;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmServiceLogic implements FilmService {

    private final FilmStorage dataFilmStorage;
    private final UserStorage dataUserStorage;

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос Post /films - {}", film.getName());
//        if (film.getLikes() == null) {
//            film.setLikes(new HashSet<>());
//        }
        return dataFilmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос Put /films - {}", film.getName());
//        if (film.getLikes() == null) {
//            film.setLikes(new HashSet<>());
//        }
        return dataFilmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        log.trace("Получен запрос Get /films");
        return dataFilmStorage.getFilms();
    }

    @Override
    public Set<Integer> addLikes(Integer filmId, Integer userId) {
        log.debug("Получен запрос PUT /films/{}/like/{} - лайк фильму", filmId, userId);
        dataUserStorage.getUser(userId);
        Film film = dataFilmStorage.getFilm(filmId);
        film.getLikes().add(userId);
        dataFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public Set<Integer> deleteLikes(Integer filmId, Integer userId) {
        log.debug("Получен запрос DELETE /films/{}/friends/{} - удаление лайка", filmId, userId);
        dataUserStorage.getUser(userId);
        Film film = dataFilmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
        dataFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public List<Film> getPopularMovies(Integer count) {
        log.trace("Получен запрос GET /films/popular?count={} - топ по лайкам", count);
        if (count == null) {
            count = Constants.DEFAULT_POPULAR_VALUE;
        }
        List<Film> films = dataFilmStorage.getFilms();
        return films.stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilm(Integer id) {
        log.trace("Получен запрос GET /films/{}", id);
        return dataFilmStorage.getFilm(id);
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

}
