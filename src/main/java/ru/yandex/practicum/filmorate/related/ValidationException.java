package ru.yandex.practicum.filmorate.related;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    public ValidationException() {
    }


    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(final String message, final IOException e) {
        super(message, e);
    }
}
