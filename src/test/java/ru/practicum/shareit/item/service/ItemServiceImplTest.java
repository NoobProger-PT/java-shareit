package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

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
    void getAll() {
        List<ItemWithBookingDto> items = itemService.getAll(1L, 0, 10);
        assertEquals(1, items.size());
    }

    @Test
    void addNew() {
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("item2");
        itemDto2.setDescription("desc2");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(null);
        itemDto2.setOwner(userDto1);
        ItemDto result = itemService.addNew(1L, itemDto2);
        assertEquals(result.getName(), itemDto2.getName());
    }

    @Test
    void addNewWithWrongUserId() {
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("item2");
        itemDto2.setDescription("desc2");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(null);
        itemDto2.setOwner(userDto1);
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemService.addNew(10L, itemDto2);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с юзером");
                    }
                });

        assertEquals("ошибка с юзером", exception.getMessage());
    }

    @Test
    void findById() {
        ItemWithBookingAndCommentDto dto = itemService.findById(1L, 1L);
        assertEquals(dto.getName(), itemDto.getName());
    }

    @Test
    void findByText() {
        List<ItemDto> items = itemService.findByText("item", 0, 10);
        assertEquals(items.size(), 1);
    }

    @Test
    void update() {
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("new2");
        itemDto2.setDescription("new2");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(null);
        ItemDto dto = itemService.update(1L, 1L, itemDto2);
        assertEquals(dto.getName(), itemDto2.getName());
    }

    @Test
    void updateWithWrongItemId() {
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("new2");
        itemDto2.setDescription("new2");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(null);
        final ItemDontExistsException exception = assertThrows(
                ItemDontExistsException.class,
                () -> {
                    try {
                        itemService.update(1L, 10L, itemDto2);
                    } catch (ItemDontExistsException e) {
                        throw new ItemDontExistsException("ошибка с item");
                    }
                });

        assertEquals("ошибка с item", exception.getMessage());
    }

    @Test
    void updateWithWrongId() {
        itemService.addNew(2L, itemDto);
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("new2");
        itemDto2.setDescription("new2");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(null);
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemService.update(1L, 2L, itemDto2);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с user");
                    }
                });

        assertEquals("ошибка с user", exception.getMessage());
    }

    @Test
    void delete() {
        itemService.delete(1L, 1L);
    }

    @Test
    void addCommentWithWrongUserId() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemService.addComment(10L, 1L, commentDto);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с user");
                    }
                });

        assertEquals("ошибка с user", exception.getMessage());
    }

    @Test
    void addCommentWithWrongArendUser() {
        UserDto userDto = new UserDto();
        userDto.setName("user10");
        userDto.setEmail("user10@mail.ru");
        userService.save(userDto);
        final RequestException exception = assertThrows(
                RequestException.class,
                () -> {
                    try {
                        itemService.addComment(3L, 1L, commentDto);
                    } catch (RequestException e) {
                        throw new RequestException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }
}