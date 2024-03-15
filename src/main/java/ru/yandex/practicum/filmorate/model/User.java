package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    int id;

    String login;
    String name;
    String email;
    LocalDate birthday;
}
