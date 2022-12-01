package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class InputItemRequestDto {
    private String description;
}
