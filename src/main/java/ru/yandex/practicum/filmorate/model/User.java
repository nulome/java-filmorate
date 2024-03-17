package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.related.ConstraintUserLogin;

import java.time.LocalDate;

@Builder
@Data
public class User {
    int id;
    @ConstraintUserLogin
    String login;
    String name;
    String email;
    LocalDate birthday;
}
