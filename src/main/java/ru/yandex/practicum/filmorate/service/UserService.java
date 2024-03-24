package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();
    Set<Integer> addUserFriend(Integer userId, Integer friendId);
    Set<Integer> deleteUserFriend(Integer userId, Integer friendId);
    Set<Integer> getFriendsList(Integer userId);
    Set<Integer> getCommonFriend(Integer userId, Integer friendId);
    User getUser(Integer id);
}
