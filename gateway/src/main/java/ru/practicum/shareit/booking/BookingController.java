package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.exception.RequestException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Positive @PathVariable long bookingId) {
        log.info("Get booking by Id");
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get booking by User");
        return bookingClient.getByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "ALL") BookingState state,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get booking by Owner");
        return bookingClient.getByOwner(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                         @Validated({Marker.Create.class}) @RequestBody InputBookingDto inputDto) {
        if (!inputDto.getStart().isBefore(inputDto.getEnd())) {
            throw new RequestException("Неверно указана дата аренды.");
        }
        log.info("Post booking");
        return bookingClient.create(bookerId, inputDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> makeApprove(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long bookingId,
                                              @RequestParam boolean approved) {
        return bookingClient.makeApprove(userId, bookingId, approved);
    }
}
