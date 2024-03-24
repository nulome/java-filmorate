package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    String error;
    String description;
}
