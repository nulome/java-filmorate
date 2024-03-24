package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userServiceLogic;

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

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userServiceLogic.getUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public Set<Integer> addUserFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userServiceLogic.addUserFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Set<Integer> deleteUserFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userServiceLogic.deleteUserFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Set<Integer> getFriendsList(@PathVariable int userId) {
        return userServiceLogic.getFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Set<Integer> getCommonFriend(@PathVariable int userId, @PathVariable int otherId) {
        return userServiceLogic.getCommonFriend(userId, otherId);
    }
}
