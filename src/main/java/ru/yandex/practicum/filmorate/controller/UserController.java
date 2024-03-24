package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceLogic;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserServiceLogic userServiceLogic;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userServiceLogic.createUser(user);
    }


    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userServiceLogic.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userServiceLogic.getUsers();
    }
}
