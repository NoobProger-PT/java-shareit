package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllBookingByOwner(userId, state);
    }

    @PostMapping
    public BookingDto createBook(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                 @Validated({Marker.Create.class}) @RequestBody InputBookingDto inputDto) {
        return bookingService.createBook(bookerId, inputDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto makeApprove(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) {
        return bookingService.makeApprove(userId, bookingId, approved);
    }
}
