package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.RequestException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        checkParameters(from, size);
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        checkParameters(from, size);
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                 @Validated({Marker.Create.class}) @RequestBody InputBookingDto inputDto) {
        if (!inputDto.getStart().isBefore(inputDto.getEnd())) {
            throw new RequestException("Неверно указана дата аренды.");
        }
        return bookingService.create(bookerId, inputDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto makeApprove(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) {
        return bookingService.makeApprove(userId, bookingId, approved);
    }

    private void checkParameters(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new RequestException("Неверно заполнены данные параметра страницы.");
        }
    }
}
