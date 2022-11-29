package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class BookingDtoTest {

    @Autowired
    JacksonTester<BookingDto> json;

    @Test
    public void shouldReturnCorrectJson() throws Exception {
        BookingDto bookingDto = new BookingDto(
                10L,
                LocalDateTime.of(2024,10,10,10,10),
                LocalDateTime.of(2025,10,10,10,10),
                new ItemDto(1L),
                new UserDto(1L),
                BookingStatus.APPROVED
        );

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-10-10T10:10:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-10-10T10:10:00");
        assertEquals(10L, bookingDto.getId());
        bookingDto.setId(15L);
        assertEquals(15L, bookingDto.getId());
    }

    @Test
    public void shouldCreateEmptyClass() {
        BookingDto bookingDto = new BookingDto();
        assertEquals(null, bookingDto.getId());
    }
}