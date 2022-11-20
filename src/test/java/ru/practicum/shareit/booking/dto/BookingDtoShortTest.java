package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoShortTest {

    private static final BookingDtoShort bookingDtoShort1 = new BookingDtoShort();
    private static final BookingDtoShort bookingDtoShort2 = new BookingDtoShort();

    @BeforeAll
    public static void createShortDto() {
        bookingDtoShort1.setBookerId(1L);
        bookingDtoShort1.setId(2L);
    }

    @Test
    void getId() {
        assertEquals(2L, bookingDtoShort1.getId());
    }

    @Test
    void getBookerId() {
        assertEquals(1L, bookingDtoShort1.getBookerId());
    }

    @Test
    void setId() {
        bookingDtoShort2.setId(10L);
        assertEquals(10L, bookingDtoShort2.getId());
    }

    @Test
    void setBookerId() {
        bookingDtoShort2.setBookerId(11L);
        assertEquals(11L, bookingDtoShort2.getBookerId());
    }
}