package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private HashMap<Integer, User> userMap = new HashMap<>();
    int id = 1;

    @PostMapping("/add")
    public User createUser(@RequestBody User user){
        userMap.put(id, user);
        id++;
        return user;
    }


    @PutMapping("/upd")
    public User updateUser(@RequestBody User user){
        userMap.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers(){
        List<User> filmList = new ArrayList<>(userMap.values());
        return filmList;
    }


//    создание пользователя;
//    обновление пользователя;
//    получение списка всех пользователей.
}
