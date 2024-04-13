package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.related.Constants;
import ru.yandex.practicum.filmorate.related.UnknownValueException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmServiceLogic implements FilmService {

    private final FilmStorage dataFilmStorage;
    private final UserService userServiceLogic;

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос Post /films - {}", film.getName());
        return dataFilmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос Put /films - {}", film.getName());
        checkAndProvideFilmInDataBase(film.getId());
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
        userServiceLogic.getUser(userId);
        Film film = checkAndProvideFilmInDataBase(filmId);
        film.getLikes().add(userId);
        dataFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public Set<Integer> deleteLikes(Integer filmId, Integer userId) {
        log.debug("Получен запрос DELETE /films/{}/friends/{} - удаление лайка", filmId, userId);
        userServiceLogic.getUser(userId);
        Film film = checkAndProvideFilmInDataBase(filmId);
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
        return checkAndProvideFilmInDataBase(id);
    }

    @Override
    public List<Genre> getGenres() {
        return dataFilmStorage.getGenres();
    }

    @Override
    public Genre getGenre(Integer id) {
        return dataFilmStorage.getGenre(id);
    }

    @Override
    public List<MPA> getMpas() {
        return dataFilmStorage.getMpas();
    }

    @Override
    public MPA getMpa(Integer id) {
        return dataFilmStorage.getMpa(id);
    }

    private int compare(Film film1, Film film2) {
        return film2.getLikes().size() - film1.getLikes().size();
    }

    private Film checkAndProvideFilmInDataBase(Integer id) {
        try {
            return dataFilmStorage.getFilm(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Ошибка в запросе к базе данных. Не найдено значение по id: {} \n {}", id, e.getMessage());
            throw new UnknownValueException("Не верный id фильма: " + id);
        }
    }
}
