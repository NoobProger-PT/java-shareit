package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final User user = new User();
    private static final User user1 = new User();

    @BeforeAll
    public static void createShortDto() {
        user.setId(1L);
        user.setName("name");
        user.setEmail("m@m.m");
    }

    @Test
    void getId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void getName() {
        assertEquals("name", user.getName());
    }

    @Test
    void getEmail() {
        assertEquals("m@m.m", user.getEmail());
    }

    @Test
    void setId() {
        user1.setId(1L);
        assertEquals(1L, user1.getId());
    }

    @Test
    void setName() {
        user1.setName("name");
        assertEquals("name", user1.getName());
    }

    @Test
    void setEmail() {
        user1.setEmail("m@m.m");
        assertEquals("m@m.m", user1.getEmail());
    }

    @Test
    void createWithId() {
        User user2 = new User(11L);
        assertEquals(11L, user2.getId());
    }
}