package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemRequestServiceImplTest {

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
}