package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class InputItemRequestDtoTest {

    @Autowired
    private JacksonTester<InputItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new InputItemRequestDto();
        dto.setDescription("desc");

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
    }
}