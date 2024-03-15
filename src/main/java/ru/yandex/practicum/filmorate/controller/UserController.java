package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private HashMap<Integer, User> userMap = new HashMap<>();
    int id = 1;

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        validation(user);
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        validation(user);
        userMap.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> filmList = new ArrayList<>(userMap.values());
        return filmList;
    }


    private void validation(User user) throws ValidationException {
        String message;
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            message = "электронная почта не может быть пустой и должна содержать символ'@'";
        } else if (user.getLogin().isEmpty() || !user.getLogin().contains(" ")) {
            message = "логин не может быть пустым и содержать пробелы";
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            message = "дата рождения не может быть в будущем";
        } else {
            return;
        }
        throw new ValidationException(message);
    }
}
