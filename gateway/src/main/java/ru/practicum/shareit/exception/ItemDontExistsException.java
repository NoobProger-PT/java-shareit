package ru.practicum.shareit.exception;

public class ItemDontExistsException extends RuntimeException {
    public ItemDontExistsException(String s) {
        super(s);
    }
}