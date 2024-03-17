package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.related.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class UserControllerTest {

    UserController userController;
    User user;
    Validator validator;

    @BeforeEach
    void create() {
        userController = new UserController();
        user = User.builder()
                .login("userLogin")
                .name("userName")
                .email("email@user")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void checkTestUserController() throws ValidationException {
        User checkUser = userController.createUser(user);
        assertEquals(1, checkUser.getId(), "Не записывается id");

        checkUser = userController.createUser(user);
        assertEquals(2, checkUser.getId(), "Не обновляется id");

        user.setName("");
        checkUser = userController.createUser(user);
        assertEquals(user.getLogin(), checkUser.getName(), "Не обновляется пустое значение имени");

        List<User> list = userController.getUsers();
        assertEquals(3, list.size(), "Не корректно возвращает список пользователей");

        user.setName("nameUpdate");
        user.setEmail("update@email");
        user.setId(1);
        userController.updateUser(user);
        assertEquals(user, userController.getUsers().get(1), "Не корректно обновляет пользователя");
    }

    @Test
    void checkValidationMethodUserController() throws ValidationException {
        User checkUser = null;
        user.setEmail("email");
        try {
            checkUser = userController.createUser(user);
        } catch (Exception ignored) {

        }
        assertNull(checkUser, "Ошибка с email без спец. символа не обрабатывается");

        user.setEmail(" ");
        try {
            checkUser = userController.createUser(user);
        } catch (Exception ignored) {

        }
        assertNull(checkUser, "Ошибка с пустым email не обрабатывается");

        user.setEmail("email@ru");
        user.setBirthday(LocalDate.of(3500,1,1));
        ValidationException exceptionDateWarn = assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        }, "Дата рождения в будущем. Ожидалось исключение ValidationException.");

        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("  ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Login не должен быть пустым");

        user.setLogin("log in");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Login не должен включать пробел");
    }
}