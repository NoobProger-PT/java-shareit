package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InputBookingDtoTest {

    private static final InputBookingDto inputBookingDto1 = new InputBookingDto();
    private static final InputBookingDto inputBookingDto2 = new InputBookingDto();

    @BeforeAll
    public static void createShortDto() {
        inputBookingDto1.setItemId(1L);
        inputBookingDto1.setStart(LocalDateTime.MIN);
        inputBookingDto1.setEnd(LocalDateTime.MAX);
    }

    @Test
    void getStart() {
        assertEquals(LocalDateTime.MIN, inputBookingDto1.getStart());
    }

    @Test
    void getEnd() {
        assertEquals(LocalDateTime.MAX, inputBookingDto1.getEnd());
    }

    @Test
    void getItemId() {
        assertEquals(1, inputBookingDto1.getItemId());
    }

    @Test
    void setStart() {
        inputBookingDto2.setStart(LocalDateTime.MIN);
        assertEquals(LocalDateTime.MIN, inputBookingDto2.getStart());
    }

    @Test
    void setEnd() {
        inputBookingDto2.setEnd(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, inputBookingDto2.getEnd());
    }

    @Test
    void setItemId() {
        inputBookingDto2.setItemId(2L);
        assertEquals(2, inputBookingDto2.getItemId());
    }
}