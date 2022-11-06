package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {


    public static Booking mapToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStartDate(bookingDto.getStart());
        booking.setEndDate(bookingDto.getEnd());
        booking.setItem(bookingDto.getItem());
        booking.setBooker(bookingDto.getBooker());
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingDto mapToDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking mapFromInputToBooking(InputBookingDto inputBookingDto, long bookerId, BookingStatus status) {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(bookerId);
        item.setId(inputBookingDto.getItemId());
        booking.setStartDate(inputBookingDto.getStart());
        booking.setEndDate(inputBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(status);
        return booking;
    }

    public static BookingDtoShort mapToShortDto(Booking b) {
        if (b == null) {
            return null;
        }
        BookingDtoShort bookingDtoShort = new BookingDtoShort();
        bookingDtoShort.setId(b.getId());
        bookingDtoShort.setBookerId(b.getBooker().getId());
        return bookingDtoShort;
    }
}
