package ru.practicum.shareit.exception;

public class InvalidItem extends RuntimeException {
    public InvalidItem(String s) {
        super(s);
    }
}