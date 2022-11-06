package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class InputBookingDto {
    @NotNull(groups = {Marker.Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Marker.Create.class})
    private LocalDateTime end;
    @NotNull(groups = {Marker.Create.class})
    private Long itemId;
}
