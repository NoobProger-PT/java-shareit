package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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

    public List<BookingDto> getAllByUser(long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId,
                        Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndDateBefore(userId,
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartDateAfter(userId,
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId,
                        BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId,
                        BookingStatus.REJECTED, Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            default:
                bookings = List.of();
        }
        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllByOwner(long userId, BookingState state) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователь с id " + userId + " не найден."));

        List<Long> userItems = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdIn(userItems,
                        Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemIdInAndStartDateBeforeAndEndDateAfter(userItems,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndDateBefore(userItems,
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartDateAfter(userItems,
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatus(userItems,
                        BookingStatus.WAITING, Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatus(userItems,
                        BookingStatus.REJECTED, Sort.by(Sort.Direction.DESC, "startDate"));
                break;
            default:
                bookings = List.of();
        }
        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
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
