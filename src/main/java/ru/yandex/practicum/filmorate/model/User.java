package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.related.ConstraintUserLogin;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Builder
@Data
public class User {
    int id;
    @ConstraintUserLogin
    String login;
    String name;
    @Email
    String email;
    //@Past
    LocalDate birthday;
}
