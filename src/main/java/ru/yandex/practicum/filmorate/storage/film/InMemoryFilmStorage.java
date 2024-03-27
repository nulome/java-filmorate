package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.related.UnknownValueException;

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
            throw new UnknownValueException("Не верный " + id + " фильма");
        }
    }
}
