package ru.practicum.shareit.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.*;

import java.util.NoSuchElementException;

class ErrorHandlerTest {

    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void invalidUser() {
        var ex = new UserDontExistsException("Ошибка");
        var result = errorHandler.invalidUser(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void invalidItem() {
        var ex = new ItemDontExistsException("Ошибка");
        var result = errorHandler.invalidItem(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void testInvalidItem() {
        var ex = new InvalidItem("Ошибка");
        var result = errorHandler.invalidItem(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void testInvalidUser() {
        var ex = new InvalidUser("Неверно заполнены данные пользователя");
        var result = errorHandler.invalidUser(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(409, result.getStatusCodeValue());
    }

    @Test
    void badRequest() {
        var ex = new RequestException("Ошибка");
        var result = errorHandler.badRequest(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void linkError() {
        var ex = new ConstraintViolationException("Не найдена связь между сущностями.", null, "name");
        var result = errorHandler.linkError(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void noElementException() {
        var ex = new NoSuchElementException("Поиск не дал результатов");
        var result = errorHandler.noElementException(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }

    @Test
    void serverException() {
        var ex = new Throwable("Ошибка сервера");
        var result = errorHandler.serverException(ex);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ex.getMessage(), result.getBody());
    }
}