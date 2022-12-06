package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private static final Comment comment = new Comment();
    private static final Comment comment1 = new Comment();

    @BeforeAll
    public static void createShortDto() {
        comment.setUser(new User());
        comment.setItem(new Item());
        comment.setCreated(LocalDateTime.MIN);
        comment.setText("text");
        comment.setId(20L);
    }

    @Test
    void getId() {
        assertEquals(20L, comment.getId());
    }

    @Test
    void getText() {
        assertEquals("text", comment.getText());
    }

    @Test
    void getItem() {
        assertNotNull(comment.getItem());
    }

    @Test
    void getUser() {
        assertNotNull(comment.getUser());
    }

    @Test
    void getCreated() {
        assertEquals(LocalDateTime.MIN, comment.getCreated());
    }

    @Test
    void setId() {
        comment1.setId(2L);
        assertEquals(2L, comment1.getId());
    }

    @Test
    void setText() {
        comment1.setText("text1");
        assertEquals("text1", comment1.getText());
    }

    @Test
    void setItem() {
        comment1.setItem(new Item());
        assertNotNull(comment1.getItem());
    }

    @Test
    void setUser() {
        comment1.setUser(new User());
        assertNotNull(comment1.getUser());
    }

    @Test
    void setCreated() {
        comment1.setCreated(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN, comment1.getCreated());
    }
}