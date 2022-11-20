package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private static final Booking booking = new Booking();

    private static final BookingDto bookingDto = new BookingDto();

    private static final BookingDtoShort shortDto = new BookingDtoShort();

    private static final InputBookingDto inputBookingDto = new InputBookingDto();

    private static final Item item = new Item();

    private static final User user = new User();

    @BeforeAll
    public static void createData() {
        user.setId(14L);
        user.setName("user");
        user.setEmail("mail@mail.re");

        item.setId(11L);
        item.setRequest(new ItemRequest(45L));
        item.setName("name");
        item.setDescription("desc");
        item.setOwner(user);
        item.setAvailable(true);

        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.MIN);
        booking.setEndDate(LocalDateTime.MAX);
        booking.setStatus(BookingStatus.APPROVED);

        bookingDto.setId(2L);
        bookingDto.setBooker(new UserDto(55L));
        bookingDto.setItem(new ItemDto(78L));
        bookingDto.setStart(LocalDateTime.MIN);
        bookingDto.setEnd(LocalDateTime.MAX);
        bookingDto.setStatus(BookingStatus.APPROVED);

        inputBookingDto.setItemId(20L);
        inputBookingDto.setStart(LocalDateTime.MIN);
        inputBookingDto.setEnd(LocalDateTime.MAX);

        shortDto.setBookerId(1L);
        shortDto.setId(10L);
    }

    @Test
    void mapToDto() {
        BookingDto dto = BookingMapper.mapToDto(booking);
        assertEquals(booking.getId(), dto.getId());
    }

    @Test
    void mapFromInputToBooking() {
        Booking booking1 = BookingMapper.mapFromInputToBooking(inputBookingDto, 23L, BookingStatus.WAITING);
        assertEquals(booking1.getItem().getId(), inputBookingDto.getItemId());
    }

    @Test
    void mapToShortDto() {
        BookingDtoShort bookingDtoShort = BookingMapper.mapToShortDto(booking);
        assertEquals(bookingDtoShort.getId(), booking.getId());
    }

    @Test
    void mapToShortDtoWithNullInput() {
        BookingDtoShort bookingDtoShort = BookingMapper.mapToShortDto(null);
        assertNull(bookingDtoShort);
    }
}