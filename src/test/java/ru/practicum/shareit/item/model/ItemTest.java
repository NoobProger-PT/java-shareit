package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private static final Item item = new Item();
    private static final Item item1 = new Item();

    @BeforeAll
    public static void createShortDto() {
        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(new User());
        item.setDescription("desc");
        item.setRequest(new ItemRequest());
        item.setName("name");
    }

    @Test
    void getId() {
        assertEquals(20L, item.getId());
    }

    @Test
    void getOwner() {
        assertNotNull(item.getOwner());
    }

    @Test
    void getName() {
        assertEquals("name", item.getName());
    }

    @Test
    void getDescription() {
        assertEquals("desc", item.getDescription());
    }

    @Test
    void isAvailable() {
        assertTrue(item.isAvailable());
    }

    @Test
    void getRequest() {
        assertNotNull(item.getRequest());
    }

    @Test
    void setId() {
        item1.setId(2L);
        assertEquals(2L, item1.getId());
    }

    @Test
    void setOwner() {
        item1.setOwner(new User());
        assertNotNull(item1.getOwner());
    }

    @Test
    void setName() {
        item1.setName("name");
        assertEquals("name", item1.getName());
    }

    @Test
    void setDescription() {
        item1.setDescription("desc");
        assertEquals("desc", item1.getDescription());
    }

    @Test
    void setAvailable() {
        item1.setAvailable(true);
        assertTrue(item1.isAvailable());
    }

    @Test
    void setRequest() {
        item1.setRequest(new ItemRequest());
        assertNotNull(item1.getRequest());
    }
}