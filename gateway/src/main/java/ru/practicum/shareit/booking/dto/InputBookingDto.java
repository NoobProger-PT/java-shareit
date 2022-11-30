package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class InputBookingDto {
    @NotNull(groups = {Marker.Create.class})
    @FutureOrPresent(groups = {Marker.Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Marker.Create.class})
    @Future(groups = {Marker.Create.class})
    private LocalDateTime end;
    @NotNull(groups = {Marker.Create.class})
    private Long itemId;
}
