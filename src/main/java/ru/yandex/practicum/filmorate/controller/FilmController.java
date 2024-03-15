package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private HashMap<Integer,Film> filmMap = new HashMap<>();
    int id = 1;

    @PostMapping("/add")
    public Film createFilm(@RequestBody Film film){
        filmMap.put(id, film);
        id++;
        return film;
    }


    @PutMapping("/upd")
    public Film updateFilm(@RequestBody Film film){
        filmMap.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms(){
        List<Film> filmList = new ArrayList<>(filmMap.values());
        return filmList;
    }


//    добавление фильма;
//    обновление фильма;
//    получение всех фильмов.
}
