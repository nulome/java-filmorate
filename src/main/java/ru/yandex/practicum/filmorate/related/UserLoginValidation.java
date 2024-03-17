package ru.yandex.practicum.filmorate.related;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidation implements ConstraintValidator<ConstraintUserLogin, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.contains(" ") && !s.isEmpty();
    }
}
