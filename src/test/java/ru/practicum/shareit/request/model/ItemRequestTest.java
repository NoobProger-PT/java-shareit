package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    private static final ItemRequest itemRequest = new ItemRequest();
    private static final ItemRequest itemRequest1 = new ItemRequest();

    @BeforeAll
    public static void createShortDto() {
        itemRequest.setId(20L);
        itemRequest.setCreated(LocalDateTime.MAX);
        itemRequest.setRequestor(new User());
        itemRequest.setDescription("desc");
    }

    @Test
    void getId() {
        assertEquals(20L, itemRequest.getId());
    }

    @Test
    void getDescription() {
        assertEquals("desc", itemRequest.getDescription());
    }

    @Test
    void getRequestor() {
        assertNotNull(itemRequest.getRequestor());
    }

    @Test
    void getCreated() {
        assertEquals(LocalDateTime.MAX, itemRequest.getCreated());
    }

    @Test
    void setId() {
        itemRequest1.setId(2L);
        assertEquals(2L, itemRequest1.getId());
    }

    @Test
    void setDescription() {
        itemRequest1.setDescription("desc");
        assertEquals("desc", itemRequest1.getDescription());
    }

    @Test
    void setRequestor() {
        itemRequest1.setRequestor(new User());
        assertNotNull(itemRequest1.getRequestor());
    }

    @Test
    void setCreated() {
        itemRequest1.setCreated(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, itemRequest1.getCreated());
    }

    @Test
    void createWithId() {
        ItemRequest itemRequest2 = new ItemRequest(12L);
        assertEquals(12L, itemRequest2.getId());
    }
}