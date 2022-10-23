package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.user.controller.UserController;

@Slf4j
@RestControllerAdvice(assignableTypes = {UserController.class, ItemController.class})
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> badValid(MethodArgumentNotValidException ex) {
        log.info("Неверно заполнены данные. {}", ex.getMessage());
        return new ResponseEntity<>("Неверно заполнены данные.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> missHeader(MissingRequestHeaderException ex) {
        log.info("Неуказан ID пользователя в HTTP запросе. {}", ex.getMessage());
        return new ResponseEntity<>("X-Sharer-User-Id не указан", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity invalidUser(final UserDontExistsException e) {
        log.info("Пользователь не найден. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity invalidItem(final ItemDontExistsException e) {
        log.info("Предмет не найден. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity invalidItem(final InvalidItem e) {
        log.info("Неверно заполнены данные вещи. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity invalidUser(final InvalidUser e) {
        log.info("Неверно заполнены данные пользователя. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> serverException(Throwable ex) {
        log.info("Ошибка на сервере. {}", ex.getMessage());
        return new ResponseEntity<>("Ошибка сервера", HttpStatus.BAD_REQUEST);
    }
}
