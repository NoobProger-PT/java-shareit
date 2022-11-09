package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;

import java.util.Objects;

@Data
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemWithBookingDto that = (ItemWithBookingDto) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
