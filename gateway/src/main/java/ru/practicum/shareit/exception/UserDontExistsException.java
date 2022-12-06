package ru.practicum.shareit.exception;

public class UserDontExistsException extends RuntimeException {
    public UserDontExistsException(String s) {
        super(s);
    }
}
