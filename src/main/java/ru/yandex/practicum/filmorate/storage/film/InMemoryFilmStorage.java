package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> filmMap = new HashMap<>();
    int id = 1;


    @Override
    public Film createFilm(Film film) {
        film.setId(id);
        filmMap.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validContains(film.getId());
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film getFilm(Integer id) {
        validContains(id);
        return filmMap.get(id);
    }

    private void validContains(Integer id) {
        if (!filmMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный " + id + " фильма");
        }
    }
}
