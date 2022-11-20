package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    private static final UserDto dto = new UserDto();
    private static final UserDto dto1 = new UserDto();

    @BeforeAll
    public static void createShortDto() {
        dto.setId(1L);
        dto.setName("name");
        dto.setEmail("m@m.m");
    }

    @Test
    void getId() {
        assertEquals(1L, dto.getId());
    }

    @Test
    void getName() {
        assertEquals("name", dto.getName());
    }

    @Test
    void getEmail() {
        assertEquals("m@m.m", dto.getEmail());
    }

    @Test
    void setId() {
        dto1.setId(10L);
        assertEquals(10L, dto1.getId());
    }

    @Test
    void setName() {
        dto1.setName("name");
        assertEquals("name", dto1.getName());
    }

    @Test
    void setEmail() {
        dto1.setEmail("m@m.m");
        assertEquals("m@m.m", dto1.getEmail());
    }

    @Test
    void createUserDtoWithId() {
        UserDto dto2 = new UserDto(2L);
        assertEquals(2L, dto2.getId());
    }
}