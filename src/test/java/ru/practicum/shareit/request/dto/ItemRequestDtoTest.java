package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new ItemRequestDto();
        dto.setId(20L);
        dto.setCreated(LocalDateTime.MAX);
        dto.setDescription("desc");
        dto.setItems(new ArrayList<>());

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((dto.getId()).intValue());
        assertThat(result).hasJsonPathValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).hasJsonPathValue("$.items");
    }

    @Test
    void addItemInList() {
        var dto = new ItemRequestDto();
        ItemDto itemDto = new ItemDto();
        assertEquals(0, dto.getItems().size());
        dto.addItemInList(itemDto);
        assertEquals(1, dto.getItems().size());
    }
}