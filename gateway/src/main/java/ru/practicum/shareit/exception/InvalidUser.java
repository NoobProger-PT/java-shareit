package ru.practicum.shareit.exception;

public class InvalidUser extends RuntimeException {
    public InvalidUser(String s) {
        super(s);
    }
}
