package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    private static final CommentDto commentDto = new CommentDto();
    private static final CommentDto commentDto1 = new CommentDto();

    @BeforeAll
    public static void createShortDto() {
        commentDto.setId(1L);
        commentDto.setCreated(LocalDateTime.MAX);
        commentDto.setAuthorName("name");
        commentDto.setText("text");
    }

    @Test
    void getId() {
        assertEquals(1, commentDto.getId());
    }

    @Test
    void getText() {
        assertEquals("text", commentDto.getText());
    }

    @Test
    void getAuthorName() {
        assertEquals("name", commentDto.getAuthorName());
    }

    @Test
    void getCreated() {
        assertEquals(LocalDateTime.MAX, commentDto.getCreated());
    }

    @Test
    void setId() {
        commentDto1.setId(1L);
        assertEquals(1, commentDto1.getId());
    }

    @Test
    void setText() {
        commentDto1.setText("text");
        assertEquals("text", commentDto1.getText());
    }

    @Test
    void setAuthorName() {
        commentDto1.setAuthorName("name");
        assertEquals("name", commentDto1.getAuthorName());
    }

    @Test
    void setCreated() {
        commentDto1.setCreated(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, commentDto1.getCreated());
    }
}