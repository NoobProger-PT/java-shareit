package ru.practicum.shareit.booking.service;

import java.util.List;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;

public interface BookingService {

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getAllBookingByUser(long userId, BookingState state);

    List<BookingDto> getAllBookingByOwner(long userId, BookingState state);

    BookingDto createBook(long userId, InputBookingDto inputDto);

    BookingDto makeApprove(long userId, long bookingId, boolean approved);
}
