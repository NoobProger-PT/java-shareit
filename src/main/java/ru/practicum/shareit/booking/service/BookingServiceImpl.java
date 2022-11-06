package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Аренды с номером id " + bookingId + " не существует."));
        if (userId != booking.getItem().getOwnerId() && userId != booking.getBooker().getId()) {
            throw new UserDontExistsException("Пользователь с id " + userId +
                    " не является арендатором или владельцем вещи.");
        }
        return BookingMapper.mapToDto(booking);
    }

    public List<BookingDto> getAllBookingByUser(long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDateDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(userId,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(userId,
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(userId,
                        BookingStatus.REJECTED);
                break;
        }

        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookingByOwner(long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Long> userItems = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdInOrderByStartDateDesc(userItems);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemIdInAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        userItems, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndDateBeforeOrderByStartDateDesc(userItems,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartDateAfterOrderByStartDateDesc(userItems,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDateDesc(userItems,
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDateDesc(userItems,
                        BookingStatus.REJECTED);
                break;
        }

        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto createBook(long userId, InputBookingDto inputDto) {
        User booker = userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));
        Item itemToBook = itemRepository.findById(inputDto.getItemId()).orElseThrow(() ->
                new ItemDontExistsException("Предмет с id " + inputDto.getItemId() + " не найден."));
        if (userId == itemToBook.getOwnerId()) {
            throw new UserDontExistsException("Арендатор является пользоателем.");
        }
        if (inputDto.getStart().isAfter(inputDto.getEnd()) || inputDto.getEnd().isBefore(inputDto.getStart())
                || inputDto.getStart().isBefore(LocalDateTime.now())) {
            throw new RequestException("Неверно указана дата аренды.");
        }
        if (!itemToBook.isAvailable()) {
            throw new InvalidItem("Предмет " + itemToBook.getName() + " уже занят.");
        }
        Booking booking = bookingRepository.save(BookingMapper.mapFromInputToBooking(
                inputDto, userId, BookingStatus.WAITING));
        booking.setBooker(booker);
        booking.setItem(itemToBook);
        return BookingMapper.mapToDto(booking);
    }

    @Transactional
    public BookingDto makeApprove(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Аренды с номером id " + bookingId + " не существует."));
        if (userId != booking.getItem().getOwnerId()) {
            throw new UserDontExistsException("Пользователь с id " + userId + " не является собственником вещи.");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new RequestException("Аренда уже подтверждена.");
        }
        if (booking.getStatus() != BookingStatus.APPROVED) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        }
        Booking reSaved = bookingRepository.save(booking);
        return BookingMapper.mapToDto(reSaved);
    }
}
