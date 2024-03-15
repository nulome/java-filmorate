package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class ValidationException extends Exception{
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(final String message, final IOException e) {
        super(message, e);
    }
}
