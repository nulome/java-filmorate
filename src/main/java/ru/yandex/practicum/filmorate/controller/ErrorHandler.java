package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.related.UnknownValueException;
import ru.yandex.practicum.filmorate.related.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("Error 400 {}", e.getMessage());
        return new ErrorResponse(
                "Ошибка данных", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("Error 400 {}", e.getMessage());
        return new ErrorResponse(
                "Передано не корректное значение", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmptyResultDataAccessException(final EmptyResultDataAccessException e) {
        log.error("Error 404 {}", e.getMessage());
        return new ErrorResponse(
                "В базу данных передано неизвестное значение", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUnknownValueException(final UnknownValueException e) {
        log.error("Error 404 {}", e.getMessage());
        return new ErrorResponse(
                "Неизвестное значение", e.getMessage()
        );
    }



    /*
    EmptyResultDataAccessException


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse(
                "Произошла непредвиденная ошибка.", e.toString()
        );
    }*/
}
