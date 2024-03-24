package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmServiceLogic implements FilmService {
    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmServiceLogic(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
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
        log.info("Получен запрос Get /films");
        return inMemoryFilmStorage.getFilms();
    }
}
