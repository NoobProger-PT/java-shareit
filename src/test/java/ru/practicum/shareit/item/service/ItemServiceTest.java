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
class ItemServiceTest {

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

    private ItemDto createItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item2");
        itemDto.setDescription("desc2");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        return itemDto;
    }

    @Test
    void getAll() {
        List<ItemWithBookingDto> items = itemService.getAll(1L, 0, 10);
        assertEquals(1, items.size());
    }



    @Test
    void addNew() {
        ItemDto itemDto2 = createItem();
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
    void findById2() {
        ItemWithBookingAndCommentDto dto = itemService.findById(1L, 10L);
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
    void update2() {
        ItemDto itemDto2 = new ItemDto();
        ItemDto dto = itemService.update(1L, 1L, itemDto2);
        assertEquals("item", dto.getName());
    }

    @Test
    void update3() {
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("");
        itemDto2.setDescription("");
        ItemDto dto = itemService.update(1L, 1L, itemDto2);
        assertEquals("item", dto.getName());
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
    /*
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
    }*/
}