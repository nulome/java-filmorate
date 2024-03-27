package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceLogic implements UserService {

    private final UserStorage inMemoryUserStorage;

    @Override
    public User createUser(User user) {
        log.info("Получен запрос Post /users - {}", user.getLogin());
        validation(user);
        user.setFriendsList(new HashSet<>());
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
        log.info("Получен запрос PUT /users/{}/friends/{}", userId, friendId);
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
        log.info("Получен запрос DELETE /users/{}/friends/{}", userId, friendId);
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        user.getFriendsList().remove(friendId);
        friend.getFriendsList().remove(userId);
        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friend);
        return user.getFriendsList();
    }

    @Override
    public List<User> getFriendsList(Integer userId) {
        log.trace("Получен запрос GET /users/{}/friends", userId);
        List<User> users = new ArrayList<>();
        for (Integer id : inMemoryUserStorage.getUser(userId).getFriendsList()) {
            users.add(inMemoryUserStorage.getUser(id));
        }
        return users;
    }

    @Override
    public List<User> getCommonFriend(Integer userId, Integer friendId) {
        log.trace("Получен запрос GET /users/{}/friends/common/{}", userId, friendId);
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        List<User> users = new ArrayList<>();
        for (Integer idList : user.getFriendsList()) {
            if (friend.getFriendsList().contains(idList)) {
                users.add(inMemoryUserStorage.getUser(idList));
            }
        }
        return users;
    }

    @Override
    public User getUser(Integer id) {
        log.trace("Получен запрос GET /users/{}", id);
        return inMemoryUserStorage.getUser(id);
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
