package ru.practicum.shareit.booking.service;

import java.util.List;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

public interface BookingService {

    BookingDto getById(long userId, long bookingId);

    List<BookingDto> getAllByUser(long userId, BookingState state);

    List<BookingDto> getAllByOwner(long userId, BookingState state);

    BookingDto create(long userId, InputBookingDto inputDto);

    BookingDto makeApprove(long userId, long bookingId, boolean approved);
}
