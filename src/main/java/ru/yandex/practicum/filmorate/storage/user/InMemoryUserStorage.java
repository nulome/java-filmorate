package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.UnknownValueException;

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
        validContains(user.getId());
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
        validContains(id);
        return userMap.get(id);
    }

    private void validContains(Integer id) {
        if (!userMap.containsKey(id)) {
            throw new UnknownValueException("Не верный " + id + " пользователя");
        }
    }
}
