package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.related.ConstraintUserLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    int id;
    @ConstraintUserLogin
    String login;
    String name;
    String email;
    LocalDate birthday;
}
