package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestService itemRequestService;

    private ItemDto itemDto = new ItemDto();
    private ItemDto itemDto2 = new ItemDto();
    private UserDto userDto = new UserDto();
    private UserDto userDto2 = new UserDto();
    private CommentDto commentDto = new CommentDto();
    private InputBookingDto nextBooking = new InputBookingDto();
    private InputBookingDto lastBooking = new InputBookingDto();

    private InputItemRequestDto itemRequestDto = new InputItemRequestDto();

    @BeforeEach
    public void createData() {
        userDto.setName("user1");
        userDto.setEmail("mauil1@masa.rt");
        userService.save(userDto);

        userDto2.setName("user2");
        userDto2.setEmail("mauil2@masa.rt");
        userService.save(userDto2);

        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        itemDto.setOwner(userDto);
        itemService.addNew(1L, itemDto);

        itemRequestDto.setDescription("хочу пиццу");
        itemRequestService.create(1L, itemRequestDto);

        itemDto2.setName("pizza");
        itemDto2.setDescription("desc");
        itemDto2.setAvailable(true);
        itemDto2.setRequestId(1L);
        itemDto2.setOwner(userDto2);
        itemService.addNew(2L, itemDto2);

        itemRequestDto.setDescription("хочу пиццу еще");
        itemRequestService.create(1L, itemRequestDto);
    }

    @Test
    void create() {
        ItemRequestDto dto = itemRequestService.create(1L, itemRequestDto);
        assertEquals(itemRequestDto.getDescription(), dto.getDescription());
    }

    @Test
    void createWithWrongUserId() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemRequestService.create(10L, itemRequestDto);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с юзером");
                    }
                });

        assertEquals("ошибка с юзером", exception.getMessage());
    }

    @Test
    void getById() {
        ItemRequestDto itemRequest = itemRequestService.getById(1L, 1L);
        assertEquals("хочу пиццу", itemRequest.getDescription());
    }

    @Test
    void getByIdWithWrongUser() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemRequestService.getById(1L, 10L);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с юзером");
                    }
                });

        assertEquals("ошибка с юзером", exception.getMessage());
    }

    @Test
    void getByIdWithWrongRequest() {
        final ItemDontExistsException exception = assertThrows(
                ItemDontExistsException.class,
                () -> {
                    try {
                        itemRequestService.getById(10L, 1L);
                    } catch (ItemDontExistsException e) {
                        throw new ItemDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getByIdWithWrongRequestItem() {
        final ItemDontExistsException exception = assertThrows(
                ItemDontExistsException.class,
                () -> {
                    try {
                        itemRequestService.getById(2L, 1L);
                    } catch (ItemDontExistsException e) {
                        throw new ItemDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getAll() {
        List<ItemRequestDto> dtos = itemRequestService.getAll(1L);
        assertEquals(2, dtos.size());
    }

    @Test
    void getAllWithWrongUser() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        itemRequestService.getAll(10L);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка");
                    }
                });

        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void getByFromAndSize() {
        List<ItemRequestDto> dtos = itemRequestService.getByFromAndSize(0, 10, 2L);
        assertEquals(2, dtos.size());
    }

    @Test
    void getByFrom1AndSize10() {
        List<ItemRequestDto> dtos = itemRequestService.getByFromAndSize(1, 10, 2L);
        assertEquals(1, dtos.size());
    }
}