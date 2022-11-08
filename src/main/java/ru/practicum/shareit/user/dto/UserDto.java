package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank(groups = {Marker.Create.class})
    private String name;
    @NotNull(groups = {Marker.Create.class})
    @Email(groups = {Marker.Create.class, Marker.Update.class})
    private String email;

    public UserDto(long id) {
        this.id = id;
    }
}