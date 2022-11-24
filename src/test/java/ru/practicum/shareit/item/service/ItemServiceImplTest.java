package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemServiceImplTest {
    private ItemServiceImpl itemService;
    private ItemRepository itemRepository;

    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private final User user = new User();
    private final Item item = new Item();
    private final ItemDto itemDto = new ItemDto();
    private final Booking booking = new Booking();
    private final ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
    private final Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");

        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        itemDto.setOwner(new UserDto());

        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(user);
        item.setDescription("desc");
        item.setRequest(new ItemRequest());
        item.setName("name");

        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.MIN);
        booking.setEndDate(LocalDateTime.MAX);
        booking.setStatus(BookingStatus.APPROVED);

        itemWithBookingDto.setId(1L);
        itemWithBookingDto.setName("name");
        itemWithBookingDto.setDescription("desc");
        itemWithBookingDto.setAvailable(true);
        itemWithBookingDto.setNextBooking(new BookingDtoShort());
        itemWithBookingDto.setLastBooking(new BookingDtoShort());

        comment.setUser(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.MIN);
        comment.setText("text");
        comment.setId(20L);
    }

    @Test
    void getAll() {
        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(Page.empty());
        var result = itemService.getAll(1L, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRepository, times(1))
                .findAllByOwnerId(anyLong(), any());
    }

    @Test
    void getAllWithBooking() {
        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(Page.empty());
        when(bookingRepository.findAllByItemIdInAndEndDateBeforeAndStatus(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));
        when(bookingRepository.findAllByItemIdInAndStartDateAfterAndStatus(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));
        var result = itemService.getAll(1L, 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRepository, times(1))
                .findAllByOwnerId(anyLong(), any());
    }

    @Test
    void addNew() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);
        var result = itemService.addNew(anyLong(), itemDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
    }

    @Test
    void addNewThrowException() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            itemService.addNew(10L, itemDto);
        });
    }

    @Test
    void findById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndEndDateBefore(anyLong(), any(), any())).thenReturn(booking);
        when(bookingRepository.findByItemIdAndStartDateAfter(anyLong(), any(), any())).thenReturn(booking);
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(Collections.singletonList(comment));
        var result = itemService.findById(1L, 1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
    }

    @Test
    void findByText() {
        when(itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(
                anyString(), anyString(), anyBoolean(), any())).thenReturn(Page.empty());
        var result = itemService.findByText("1L", 0, 10);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRepository, times(1))
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(
                        anyString(), anyString(), anyBoolean(), any());
    }

    @Test
    void update() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        var result = itemService.update(1L, 1L, itemDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
    }

    @Test
    void updateWithWrongItemId() {
        Assertions.assertThrows(ItemDontExistsException.class, () -> {
            itemService.update(1L, 1L, itemDto);
        });
    }

    @Test
    void updateWithWrongUser() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
            itemService.update(10L, 1L, itemDto);
        });
    }

    @Test
    void delete() {
        var itemId = 2L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        itemService.delete(1L, itemId);
        verify(userRepository, times(0)).delete(any());
    }

    @Test
    void addComment() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findByItemIdAndBookerIdAndEndDateBefore(anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(booking));
        when(commentRepository.save(any())).thenReturn(comment);
        var result = itemService.addComment(1L, 1L, new CommentDto());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(comment.getId(), result.getId());
    }

    @Test
    void addCommentWithWrongUser() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            itemService.addComment(1L, 1L, new CommentDto());
        });
    }

    @Test
    void addCommentWithWrongBooking() {
        Assertions.assertThrows(RequestException.class, () -> {
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            itemService.addComment(1L, 1L, new CommentDto());
        });
    }
}