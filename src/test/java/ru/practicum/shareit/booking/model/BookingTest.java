package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private static final Booking booking = new Booking();
    private static final Booking booking2 = new Booking();

    @BeforeAll
    public static void createData() {
        booking.setId(1L);
        booking.setBooker(new User());
        booking.setItem(new Item());
        booking.setStartDate(LocalDateTime.MIN);
        booking.setEndDate(LocalDateTime.MAX);
        booking.setStatus(BookingStatus.APPROVED);
    }

    @Test
    void getId() {
        assertEquals(1, booking.getId());
    }

    @Test
    void getStartDate() {
        assertEquals(LocalDateTime.MIN, booking.getStartDate());
    }

    @Test
    void getEndDate() {
        assertEquals(LocalDateTime.MAX, booking.getEndDate());
    }

    @Test
    void getItem() {
        assertNotNull(booking.getItem());
    }

    @Test
    void getBooker() {
        assertNotNull(booking.getBooker());
    }

    @Test
    void getStatus() {
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void setId() {
        booking2.setId(1L);
        assertEquals(1, booking2.getId());
    }

    @Test
    void setStartDate() {
        booking2.setStartDate(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN, booking2.getStartDate());
    }

    @Test
    void setEndDate() {
        booking2.setEndDate(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, booking2.getEndDate());
    }

    @Test
    void setItem() {
        booking2.setItem(new Item());
        assertNotNull(booking2.getItem());
    }

    @Test
    void setBooker() {
        booking2.setBooker(new User());
        assertNotNull(booking2.getBooker());
    }

    @Test
    void setStatus() {
        booking2.setStatus(BookingStatus.APPROVED);
        assertEquals(BookingStatus.APPROVED, booking2.getStatus());
    }
}