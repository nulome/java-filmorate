package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> userMap = new HashMap<>();
    int id = 1;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Получен запрос Post /users - {}", user.getLogin());
        validation(user);
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws ValidationException {
        log.info("Получен запрос Put /users - {}", user.getLogin());
        validation(user);
        if (!userMap.containsKey(user.getId())) {
            log.warn("Не верный id пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный id пользователя");
        }
        userMap.put(user.getId(), user);

        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос Get /users");
        List<User> filmList = new ArrayList<>(userMap.values());
        return filmList;
    }


    private void validation(User user) throws ValidationException {
        checkName(user);
        String message;
        if (user.getBirthday().isAfter(LocalDate.now())) {
            message = "дата рождения не может быть в будущем";
        } else {
            return;
        }
        log.warn(message);
        throw new ValidationException(message);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
