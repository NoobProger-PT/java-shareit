package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new ItemDto();
        dto.setId(1L);
        dto.setOwner(new UserDto());
        dto.setName("name");
        dto.setDescription("desc");
        dto.setAvailable(true);
        dto.setRequestId(5L);

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.owner");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((dto.getId()).intValue());
        assertThat(result).hasJsonPathValue("$.owner");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available");
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo((dto.getRequestId()).intValue());
    }

    @Test
    void createWithOnlyId() {
        ItemDto dto = new ItemDto(23L);
        assertEquals(23L, dto.getId());
    }
}