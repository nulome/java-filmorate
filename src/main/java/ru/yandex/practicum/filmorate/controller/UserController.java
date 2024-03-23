package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Получен запрос Post /users - {}", user.getLogin());
        return inMemoryUserStorage.createUser(user);
    }


    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Получен запрос Put /users - {}", user.getLogin());
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос Get /users");
        return inMemoryUserStorage.getUsers();
    }

}
