package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.exception.InvalidItem;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    private ItemDto itemDto = new ItemDto();
    private UserDto userDto1 = new UserDto();
    private UserDto userDto2 = new UserDto();
    private CommentDto commentDto = new CommentDto();
    private InputBookingDto nextBooking = new InputBookingDto();
    private InputBookingDto lastBooking = new InputBookingDto();

    @BeforeEach
    public void createData() {
        userDto1.setName("user1");
        userDto1.setEmail("mauil1@masa.rt");
        userService.save(userDto1);

        userDto2.setName("user2");
        userDto2.setEmail("mauil2@masa.rt");
        userService.save(userDto2);

        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        itemDto.setOwner(userDto1);
        itemService.addNew(1L, itemDto);

        lastBooking.setItemId(1L);
        lastBooking.setStart(LocalDateTime.of(2020,10,10,10,10));
        lastBooking.setEnd(LocalDateTime.of(2021,12,10,10,10));
        bookingService.create(2L, lastBooking);
        bookingService.makeApprove(1L, 1L, true);

        nextBooking.setItemId(1L);
        nextBooking.setStart(LocalDateTime.of(2023,10,10,10,10));
        nextBooking.setEnd(LocalDateTime.of(2024,12,10,10,10));
        bookingService.create(2L, nextBooking);
        bookingService.makeApprove(1L, 2L, true);

        commentDto.setText("good");
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setAuthorName("lalka");
        itemService.addComment(2L, 1L, commentDto);
    }

    @Test
    void getById() {
        BookingDto dto = bookingService.getById(1L, 1L);
        assertEquals(dto.getBooker().getEmail(), userDto2.getEmail());
    }

    @Test
    void getByIdWithWrongBookingId() {
        final NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> {
                    try {
                        bookingService.getById(1L, 10L);
                    } catch (NoSuchElementException e) {
                        throw new NoSuchElementException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getByIdWithWrongUserId() {
        UserDto userDto = new UserDto();
        userDto.setName("user10");
        userDto.setEmail("mauil10@masa.rt");
        userService.save(userDto);
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.getById(3L, 1L);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getAllByUserAllState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.ALL, 0, 10);
        assertEquals(2, bookings.size());
    }

    @Test
    void getAllByUserCurrentState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.CURRENT, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByUserPastState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.PAST, 0, 10);
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllByUserFutureState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.FUTURE, 0, 10);
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllByUserWaitingState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.WAITING, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByUserRejectedState() {
        List<BookingDto> bookings = bookingService.getAllByUser(2L, BookingState.REJECTED, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByWrongUser() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.getAllByUser(20L, BookingState.REJECTED, 0, 10);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getAllByOwnerAllState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.ALL, 0, 10);
        assertEquals(2, bookings.size());
    }

    @Test
    void getAllByOwnerCurrentState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.CURRENT, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByOwnerPastState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.PAST, 0, 10);
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllByOwnerFutureState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.FUTURE, 0, 10);
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllByOwnerWaitingState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.WAITING, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByOwnerRejectedState() {
        List<BookingDto> bookings = bookingService.getAllByOwner(1L, BookingState.REJECTED, 0, 10);
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllByWrongOwner() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.getAllByOwner(20L, BookingState.REJECTED, 0, 10);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void create() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(1L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        BookingDto dto = bookingService.create(2L, inputDto1);
        assertEquals(inputDto1.getItemId(), dto.getItem().getId());
    }

    @Test
    void createWithWrongUser() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(1L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.create(20L, inputDto1);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void createWithOwner() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(1L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.create(1L, inputDto1);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void createWithNotAvailableItem() {
        ItemDto dto = new ItemDto();
        dto.setName("item3");
        dto.setDescription("desc3");
        dto.setOwner(userDto1);
        dto.setAvailable(false);
        itemService.addNew(1L, dto);
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(2L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        final InvalidItem exception = assertThrows(
                InvalidItem.class,
                () -> {
                    try {
                        bookingService.create(2L, inputDto1);
                    } catch (InvalidItem e) {
                        throw new InvalidItem("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void createWithWrongItem() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(20L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        final ItemDontExistsException exception = assertThrows(
                ItemDontExistsException.class,
                () -> {
                    try {
                        bookingService.create(2L, inputDto1);
                    } catch (ItemDontExistsException e) {
                        throw new ItemDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void makeApprove() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(1L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        bookingService.create(2L, inputDto1);
        BookingDto dto = bookingService.makeApprove(1L, 3L, true);
        assertEquals(BookingStatus.APPROVED, dto.getStatus());
    }

    @Test
    void makeApproveWithWrongBookingId() {
        final NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> {
                    try {
                        bookingService.makeApprove(1L, 3L, true);
                    } catch (NoSuchElementException e) {
                        throw new NoSuchElementException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void makeApproveWithWrongOwnerId() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        bookingService.makeApprove(10L, 2L, true);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void makeApproveWithApprovedBooking() {
        final RequestException exception = assertThrows(
                RequestException.class,
                () -> {
                    try {
                        bookingService.makeApprove(1L, 1L, true);
                    } catch (RequestException e) {
                        throw new RequestException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void dontApproveBooking() {
        InputBookingDto inputDto1 = new InputBookingDto();
        inputDto1.setItemId(1L);
        inputDto1.setStart(LocalDateTime.of(2030,10,10,10,10));
        inputDto1.setEnd(LocalDateTime.of(2040,10,10,10,10));
        bookingService.create(2L, inputDto1);
        BookingDto dto = bookingService.makeApprove(1L, 3L, false);
        assertEquals(BookingStatus.REJECTED, dto.getStatus());
    }
}