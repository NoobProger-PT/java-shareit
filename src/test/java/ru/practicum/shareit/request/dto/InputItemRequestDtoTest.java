package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InputItemRequestDtoTest {

    private static final InputItemRequestDto dto = new InputItemRequestDto();
    private static final InputItemRequestDto dto1 = new InputItemRequestDto();

    @BeforeAll
    public static void createShortDto() {
        dto.setDescription("desc");
    }

    @Test
    void getDescription() {
        assertEquals("desc", dto.getDescription());
    }

    @Test
    void setDescription() {
        dto1.setDescription("desc1");
        assertEquals("desc1", dto1.getDescription());
    }
}