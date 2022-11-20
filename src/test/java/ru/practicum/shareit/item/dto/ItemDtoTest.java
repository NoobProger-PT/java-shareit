package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    private static final ItemDto itemDto = new ItemDto();
    private static final ItemDto itemDto1 = new ItemDto();

    @BeforeAll
    public static void createShortDto() {
        itemDto.setId(1L);
        itemDto.setOwner(new UserDto());
        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(5L);
    }

    @Test
    void getId() {
        assertEquals(1L, itemDto.getId());
    }

    @Test
    void getOwner() {
        assertNotNull(itemDto.getOwner());
    }

    @Test
    void getName() {
        assertEquals("name", itemDto.getName());
    }

    @Test
    void getDescription() {
        assertEquals("desc", itemDto.getDescription());
    }

    @Test
    void getAvailable() {
        assertTrue(itemDto.getAvailable());
    }

    @Test
    void getRequestId() {
        assertEquals(5L, itemDto.getRequestId());
    }

    @Test
    void setId() {
        itemDto1.setId(11L);
        assertEquals(11L, itemDto1.getId());
    }

    @Test
    void setOwner() {
        itemDto1.setOwner(new UserDto());
        assertNotNull(itemDto1.getOwner());
    }

    @Test
    void setName() {
        itemDto1.setName("name");
        assertEquals("name", itemDto1.getName());
    }

    @Test
    void setDescription() {
        itemDto1.setDescription("desc");
        assertEquals("desc", itemDto1.getDescription());
    }

    @Test
    void setAvailable() {
        itemDto1.setAvailable(true);
        assertTrue(itemDto1.getAvailable());
    }

    @Test
    void setRequestId() {
        itemDto1.setRequestId(5L);
        assertEquals(5L, itemDto1.getRequestId());
    }

    @Test
    void createWithOnlyId() {
        ItemDto dto = new ItemDto(23L);
        assertEquals(23L, dto.getId());
    }
}