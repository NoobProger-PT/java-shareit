package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.NotBlank;

@Data
public class InputItemRequestDto {
    @NotBlank(groups = {Marker.Create.class})
    private String description;
}
