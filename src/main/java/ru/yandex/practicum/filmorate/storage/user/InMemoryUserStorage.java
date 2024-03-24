package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> userMap = new HashMap<>();
    int id = 1;


    @Override
    public User createUser(User user) {
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный " + user.getId() + " пользователя");
        }
        userMap.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> filmList = new ArrayList<>(userMap.values());
        return filmList;
    }

    @Override
    public User getUser(Integer id) {
        if (!userMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не верный " + id + " пользователя");
        }
        return userMap.get(id);
    }

}
