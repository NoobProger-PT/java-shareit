package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new CommentDto();
        dto.setCreated(LocalDateTime.now());
        dto.setId(1L);
        dto.setAuthorName("dino");
        dto.setText("pizza");

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.text");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((dto.getId()).intValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
        assertThat(result).hasJsonPathValue("$.created");
    }
}