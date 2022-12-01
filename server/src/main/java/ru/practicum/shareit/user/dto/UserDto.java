package ru.practicum.shareit.user.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    public UserDto(long id) {
        this.id = id;
    }
}