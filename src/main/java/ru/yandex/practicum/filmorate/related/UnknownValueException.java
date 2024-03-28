package ru.yandex.practicum.filmorate.related;

import java.io.IOException;

public class UnknownValueException extends RuntimeException {
    public UnknownValueException(String message) {
        super(message);
    }

    public UnknownValueException(final String message, final IOException e) {
        super(message, e);
    }
}
