package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemWithBookingAndCommentDtoTest {

    private static final ItemWithBookingAndCommentDto dto = new ItemWithBookingAndCommentDto();
    private static final ItemWithBookingAndCommentDto dto1 = new ItemWithBookingAndCommentDto();

    @BeforeAll
    public static void createShortDto() {
        dto.setId(1L);
        dto.setName("name");
        dto.setDescription("desc");
        dto.setAvailable(true);
        dto.setComments(List.of());
        dto.setNextBooking(new BookingDtoShort());
        dto.setLastBooking(new BookingDtoShort());
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
    void getDescription() {
        assertEquals("desc", dto.getDescription());
    }

    @Test
    void getAvailable() {
        assertTrue(dto.getAvailable());
    }

    @Test
    void getLastBooking() {
        assertNotNull(dto.getLastBooking());
    }

    @Test
    void getNextBooking() {
        assertNotNull(dto.getLastBooking());
    }

    @Test
    void getComments() {
        assertNotNull(dto.getLastBooking());
    }

    @Test
    void setId() {
        dto1.setId(11L);
        assertEquals(11L, dto1.getId());
    }

    @Test
    void setName() {
        dto1.setName("name");
        assertEquals("name", dto1.getName());
    }

    @Test
    void setDescription() {
        dto1.setDescription("desc");
        assertEquals("desc", dto1.getDescription());
    }

    @Test
    void setAvailable() {
        dto1.setAvailable(true);
        assertTrue(dto1.getAvailable());
    }

    @Test
    void setLastBooking() {
        dto1.setLastBooking(new BookingDtoShort());
        assertNotNull(dto1.getLastBooking());
    }

    @Test
    void setNextBooking() {
        dto1.setNextBooking(new BookingDtoShort());
        assertNotNull(dto1.getNextBooking());
    }

    @Test
    void setComments() {
        dto1.setComments(List.of());
        assertNotNull(dto1.getComments());
    }
}