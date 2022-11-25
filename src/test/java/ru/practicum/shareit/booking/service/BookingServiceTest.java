package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidItem;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    private final Booking booking = new Booking();
    private final User user = new User();
    private final Item item = new Item();
    private final Item item2 = new Item();
    private final InputBookingDto inputBookingDto = new InputBookingDto();

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        when(bookingRepository.save(any())).then(invocation -> invocation.getArgument(0));

        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");

        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(user);
        item.setDescription("desc");
        item.setRequest(new ItemRequest());
        item.setName("name");

        item2.setId(30L);
        item2.setAvailable(false);
        item2.setOwner(user);
        item2.setDescription("desc");
        item2.setRequest(new ItemRequest());
        item2.setName("name");

        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.MIN);
        booking.setEndDate(LocalDateTime.MAX);
        booking.setStatus(BookingStatus.APPROVED);

        inputBookingDto.setEnd(LocalDateTime.MAX);
        inputBookingDto.setStart(LocalDateTime.MIN);
        inputBookingDto.setItemId(10L);
    }

    @Test
    void getById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        var result = bookingService.getById(1L, 0L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getByIdWithWrongBooking() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            bookingService.getById(1L, 0L);
        });
    }

    @Test
    void getByIdWithWrongUser() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.getById(10L, 0L);
        });
    }

    @Test
    void getAllByUserAllState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerId(anyLong(), (Pageable) any())).thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.ALL, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findAllByBookerId(anyLong(), (Pageable) any());
    }

    @Test
    void getAllByUserCurrentState() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(anyLong(),
                any(), any(), (Pageable) any())).thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.CURRENT, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStartDateBeforeAndEndDateAfter(anyLong(), any(), any(), (Pageable) any());
    }

    @Test
    void getAllByUserPastState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndDateBefore(anyLong(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.PAST, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndEndDateBefore(anyLong(), any(), (Pageable) any());
    }

    @Test
    void getAllByUserFutureState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartDateAfter(anyLong(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.FUTURE, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStartDateAfter(anyLong(), any(), (Pageable) any());
    }

    @Test
    void getAllByUserWaitingState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.WAITING, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatus(anyLong(), any(), (Pageable) any());
    }

    @Test
    void getAllByUserRejectedState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByUser(1L, BookingState.REJECTED, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatus(anyLong(), any(), (Pageable) any());
    }

    @Test
    void getAllByUserWithWrongUser() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.getAllByUser(1L, BookingState.ALL, 0, 10);
        });
    }

    @Test
    void getAllByOwnerWithWrongUser() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.getAllByOwner(1L, BookingState.ALL, 0, 10);
        });
    }

    @Test
    void getAllByOwnerAllState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdIn(any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.ALL, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdIn(any(), (Pageable) any());
    }

    @Test
    void getAllByOwnerCurrentState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdInAndStartDateBeforeAndEndDateAfter(any(), any(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.CURRENT, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdInAndStartDateBeforeAndEndDateAfter(any(), any(), any(), (Pageable) any());
    }

    @Test
    void getAllByOwnerPastState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdInAndEndDateBefore(any(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.PAST, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdInAndEndDateBefore(any(), any(), (Pageable) any());
    }

    @Test
    void getAllByOwnerFutureState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdInAndStartDateAfter(any(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.FUTURE, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdInAndStartDateAfter(any(), any(), (Pageable) any());
    }

    @Test
    void getAllByOwnerWaitingState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdInAndStatus(any(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.WAITING, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdInAndStatus(any(), any(), (Pageable) any());
    }

    @Test
    void getAllByOwnerRejectedState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(Collections.singletonList(item));
        when(bookingRepository.findAllByItemIdInAndStatus(any(), any(), (Pageable) any()))
                .thenReturn(Page.empty());
        var result = bookingService.getAllByOwner(1L, BookingState.REJECTED, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(bookingRepository, times(1))
                .findAllByItemIdInAndStatus(any(), any(), (Pageable) any());
    }

    @Test
    void create() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);
        var result = bookingService.create(10L, inputBookingDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(booking.getId(), result.getId());
    }

    @Test
    void createWithWrongBooker() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.create(1L, inputBookingDto);
        });
    }

    @Test
    void createWithWrongItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(ItemDontExistsException.class, () -> {
            bookingService.create(1L, inputBookingDto);
        });
    }

    @Test
    void createWithWrongUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.create(1L, inputBookingDto);
        });
    }

    @Test
    void createWithNotAvailableItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item2));
        Assertions.assertThrows(InvalidItem.class, () -> {
            bookingService.create(10L, inputBookingDto);
        });
    }

    @Test
    void makeApproveWithApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(RequestException.class, () -> {
            bookingService.makeApprove(1L, 1L, true);
        });
    }

    @Test
    void notMakeApprove() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        var result = bookingService.makeApprove(1L, 1L, false);
        booking.setStatus(BookingStatus.APPROVED);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void makeApprove() {
        booking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        var result = bookingService.makeApprove(1L, 1L, true);
        booking.setStatus(BookingStatus.APPROVED);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void makeApproveWithWrongBooking() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            bookingService.makeApprove(1L, 1L, true);
        });
    }

    @Test
    void makeApproveWithWrongUser() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            bookingService.makeApprove(10L, 1L, true);
        });
    }
}