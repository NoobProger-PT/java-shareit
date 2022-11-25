package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.user.controller.UserController;

import java.util.NoSuchElementException;


@Slf4j
@RestControllerAdvice(assignableTypes = {UserController.class, ItemController.class, BookingController.class,
                                        ItemRequestController.class})
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
    public ResponseEntity<String> invalidUser(final UserDontExistsException e) {
        log.info("Пользователь не найден. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> invalidItem(final ItemDontExistsException e) {
        log.info("Предмет не найден. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> invalidItem(final InvalidItem e) {
        log.info("Неверно заполнены данные вещи. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> invalidUser(final InvalidUser e) {
        log.info("Неверно заполнены данные пользователя. {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse("Неверно заполнены данные пользователя"), HttpStatus.CONFLICT);
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequest(final RequestException e) {
        log.info("Неверно заполнены данные. {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> linkError(final ConstraintViolationException e) {
        log.info("Ошибка в ссылочной связи. {}", e.getMessage());
        return new ResponseEntity<>("Не найдена связь между сущностями.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> noElementException(final NoSuchElementException e) {
        log.info("Нет совпадений. {}", e.getMessage());
        return new ResponseEntity<>("Поиск не дал результатов", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> illegalException(final MethodArgumentTypeMismatchException e) {
        String exceptionName = "Unknown state: UNSUPPORTED_STATUS";
        log.info("Переданы неверные данные. {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(exceptionName), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> serverException(Throwable ex) {
        log.info("Ошибка на сервере. {}", ex.getMessage());
        return new ResponseEntity<>("Ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse {
        private final String error;
    }
}
