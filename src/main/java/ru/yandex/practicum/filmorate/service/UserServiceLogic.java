package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceLogic implements UserService {

    UserStorage inMemoryUserStorage;
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
        log.trace("Получен запрос Get /users");
        return inMemoryUserStorage.getUsers();
    }

    @Override
    public Set<Integer> addUserFriend(Integer userId, Integer friendId) {
        log.info("Получен запрос ***");
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        user.getFriendsList().add(friendId);
        friend.getFriendsList().add(userId);
        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);
        return user.getFriendsList();
    }

    @Override
    public Set<Integer> deleteUserFriend(Integer userId, Integer friendId) {
        log.info("Получен запрос ***");
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        user.getFriendsList().remove(friendId);
        friend.getFriendsList().remove(userId);
        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);
        return user.getFriendsList();
    }

    @Override
    public Set<Integer> getFriendsList(Integer userId) {
        log.trace("Получен запрос ***");
        return inMemoryUserStorage.getUser(userId).getFriendsList();
    }

    @Override
    public Set<Integer> getCommonFriend(Integer userId, Integer friendId) {
        log.trace("Получен запрос ***");
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        Set<Integer> commonList = new HashSet<>();
        for (Integer idList : user.getFriendsList()) {
            if (friend.getFriendsList().contains(idList)) {
                commonList.add(idList);
            }
        }
        return commonList;
    }

    private void validation(User user) {
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
