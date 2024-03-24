package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceLogic implements UserService {

    InMemoryUserStorage inMemoryUserStorage;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public User createUser(User user) {
        log.info("Получен запрос Post /users - {}", user.getLogin());
        validation(user);
        return inMemoryUserStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Получен запрос Put /users - {}", user.getLogin());
        validation(user);
        return inMemoryUserStorage.updateUser(user);
    }

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос Get /users");
        return inMemoryUserStorage.getUsers();
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
