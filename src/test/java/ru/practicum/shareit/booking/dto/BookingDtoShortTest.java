package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoShortTest {
    @Autowired
    private JacksonTester<BookingDtoShort> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new BookingDtoShort();
        dto.setBookerId(1L);
        dto.setId(2L);

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo((dto.getBookerId()).intValue());
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((dto.getId()).intValue());
    }
}