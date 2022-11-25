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
        List<ItemRequestDto> dtos = itemRequestService.getByFromAndSize(0, 10, 2L);
        assertEquals(2, dtos.size());
        List<ItemRequestDto> dtos1 = itemRequestService.getByFromAndSize(1, 10, 2L);
        assertEquals(0, dtos1.size());
    }
    /*

    private ItemRequestRepository itemRequestRepository;
    private ItemRequestService itemRequestService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    private final User user = new User();
    private final Item item = new Item();
    private final ItemRequest itemRequest = new ItemRequest();
    private final InputItemRequestDto inputItemRequestDto = new InputItemRequestDto();


    @BeforeEach
    void setUp() {
        itemRequestRepository = mock(ItemRequestRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRepository, userRepository, itemRequestRepository);
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));

        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");

        itemRequest.setId(1L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("desc");
        itemRequest.setRequestor(user);

        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(user);
        item.setDescription("desc");
        item.setRequest(new ItemRequest());
        item.setName("name");

        inputItemRequestDto.setDescription("desc");
    }

    @Test
    void create() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        var result = itemRequestService.create(1L, inputItemRequestDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemRequest.getId(), result.getId());
    }

    @Test
    void getById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(Optional.of(item));
        var result = itemRequestService.getById(1L, 1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemRequest.getId(), result.getId());
    }

    @Test
    void getByIdWithWrongUser() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            itemRequestService.getById(1L, 1L);
        });
    }

    @Test
    void getByIdWithWrongRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(ItemDontExistsException.class, () -> {
            itemRequestService.getById(1L, 1L);
        });
    }

    @Test
    void getByIdWithWrongItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        Assertions.assertThrows(ItemDontExistsException.class, () -> {
            itemRequestService.getById(1L, 1L);
        });
    }

    @Test
    void getAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequestorId(anyLong(), any()))
                .thenReturn(Collections.singletonList(itemRequest));
        when(itemRepository.findAllByRequestIdIn(Collections.singletonList(anyLong())))
                .thenReturn(Collections.singletonList(item));
        var result = itemRequestService.getAll(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemRequest.getId(), result.get(0).getId());
    }

    @Test
    void getByFromAndSize() {
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(Page.empty());
        when(itemRepository.findAllByRequestIdIn(Collections.singletonList(anyLong())))
                .thenReturn(Collections.singletonList(item));
        var result = itemRequestService.getByFromAndSize(0, 10, 1L);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        verify(itemRequestRepository, times(1))
                .findAll((Pageable) any());
    }

     */
}