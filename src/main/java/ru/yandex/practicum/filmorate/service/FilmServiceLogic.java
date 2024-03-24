package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
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
    public Set<Integer> addLikes(Integer idFilm, Integer idUser) {
        log.debug("Получен запрос ***");
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.getLikes().add(idUser);
        inMemoryFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public Set<Integer> deleteLikes(Integer idFilm, Integer idUser) {
        log.debug("Получен запрос ***");
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.getLikes().remove(idUser);
        inMemoryFilmStorage.updateFilm(film);
        return film.getLikes();
    }

    @Override
    public List<Film> getPopularMovies(Integer count) {
        log.trace("Получен запрос ***");
        List<Film> films = inMemoryFilmStorage.getFilms();
        return films.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
