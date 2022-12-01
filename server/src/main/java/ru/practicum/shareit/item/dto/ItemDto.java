package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private UserDto owner;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    public ItemDto(long id) {
        this.id = id;
    }
}