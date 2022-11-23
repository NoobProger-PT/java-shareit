package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public BookingDto getById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Аренды с номером id " + bookingId + " не существует."));
        if (userId != booking.getItem().getOwner().getId() && userId != booking.getBooker().getId()) {
            throw new UserDontExistsException("Пользователь с id " + userId +
                    " не является арендатором или владельцем вещи.");
        }
        return BookingMapper.mapToDto(booking);
    }

    public List<BookingDto> getAllByUser(long userId, BookingState state, int from, int size) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                Page<Booking> pages1 = bookingRepository.findAllByBookerId(userId,
                        PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages1.stream().collect(Collectors.toList());
                break;
            case CURRENT:
                Page<Booking> pages2 = bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(),
                                PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages2.stream().collect(Collectors.toList());
                break;
            case PAST:
                Page<Booking> pages3 = bookingRepository.findAllByBookerIdAndEndDateBefore(userId,
                        LocalDateTime.now(), PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages3.stream().collect(Collectors.toList());
                break;
            case FUTURE:
                Page<Booking> pages4 = bookingRepository.findAllByBookerIdAndStartDateAfter(userId,
                        LocalDateTime.now(), PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages4.stream().collect(Collectors.toList());
                break;
            case WAITING:
                Page<Booking> pages5 = bookingRepository.findAllByBookerIdAndStatus(userId,
                        BookingStatus.WAITING, PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages5.stream().collect(Collectors.toList());
                break;
            case REJECTED:
                Page<Booking> pages6 = bookingRepository.findAllByBookerIdAndStatus(userId,
                        BookingStatus.REJECTED,
                        PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages6.stream().collect(Collectors.toList());
                break;
        }
        return bookings.stream().map(BookingMapper::mapToDto).collect(Collectors.toList());
    }

    public List<BookingDto> getAllByOwner(long userId, BookingState state, int from, int size) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Long> userItems = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings = new ArrayList<>();

        switch (state) {
            case ALL:
                Page<Booking> pages1 = bookingRepository.findAllByItemIdIn(userItems,
                                PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages1.stream().collect(Collectors.toList());
                break;
            case CURRENT:
                Page<Booking> pages2 = bookingRepository.findAllByItemIdInAndStartDateBeforeAndEndDateAfter(userItems,
                        LocalDateTime.now(), LocalDateTime.now(),
                        PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages2.stream().collect(Collectors.toList());
                break;
            case PAST:
                Page<Booking> pages3 = bookingRepository.findAllByItemIdInAndEndDateBefore(userItems,
                        LocalDateTime.now(), PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages3.stream().collect(Collectors.toList());
                break;
            case FUTURE:
                Page<Booking> pages4 = bookingRepository.findAllByItemIdInAndStartDateAfter(userItems,
                        LocalDateTime.now(), PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages4.stream().collect(Collectors.toList());
                break;
            case WAITING:
                Page<Booking> pages5 = bookingRepository.findAllByItemIdInAndStatus(userItems,
                        BookingStatus.WAITING, PageRequest.of(from, size, Sort.by("startDate").descending()));
                bookings = pages5.stream().collect(Collectors.toList());
                break;
            case REJECTED:
                Page<Booking> pages6 = bookingRepository.findAllByItemIdInAndStatus(userItems,
                        BookingStatus.REJECTED, PageRequest.of(from, size,
                                Sort.by("startDate").descending()));
                bookings = pages6.stream().collect(Collectors.toList());
                break;
        }
        return bookings.stream().map(BookingMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public BookingDto create(long userId, InputBookingDto inputDto) {
        User booker = userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));
        Item itemToBook = itemRepository.findById(inputDto.getItemId()).orElseThrow(() ->
                new ItemDontExistsException("Предмет с id " + inputDto.getItemId() + " не найден."));
        if (userId == itemToBook.getOwner().getId()) {
            throw new UserDontExistsException("Арендатор является пользоателем.");
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
        if (userId != booking.getItem().getOwner().getId()) {
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
        return BookingMapper.mapToDto(booking);
    }
}
