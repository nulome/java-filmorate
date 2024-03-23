package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> userMap = new HashMap<>();
    int id = 1;


    @Override
    public User createUser(User user) {
        validation(user);
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        validation(user);
        if (!userMap.containsKey(user.getId())) {
            log.warn("Не верный id пользователя");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный id пользователя");
        }
        userMap.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> getUsers() {
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
