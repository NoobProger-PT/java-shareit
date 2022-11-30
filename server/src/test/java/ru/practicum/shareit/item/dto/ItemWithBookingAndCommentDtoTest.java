package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemWithBookingAndCommentDtoTest {

    @Autowired
    private JacksonTester<ItemWithBookingAndCommentDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new ItemWithBookingAndCommentDto();
        dto.setId(1L);
        dto.setName("name");
        dto.setDescription("desc");
        dto.setAvailable(true);
        dto.setComments(List.of());
        dto.setNextBooking(new BookingDtoShort());
        dto.setLastBooking(new BookingDtoShort());

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((dto.getId()).intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available");
        assertThat(result).hasJsonPathValue("$.comments");
        assertThat(result).hasJsonPathValue("$.nextBooking");
        assertThat(result).hasJsonPathValue("$.lastBooking");
    }
}