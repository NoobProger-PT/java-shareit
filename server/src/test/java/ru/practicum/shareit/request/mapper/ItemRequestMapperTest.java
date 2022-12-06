package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    private static final ItemRequest itemRequest = new ItemRequest();
    private static final InputItemRequestDto itemRequestDto = new InputItemRequestDto();
    private static final User user = new User();

    @BeforeAll
    public static void createShortDto() {
        itemRequest.setId(20L);
        itemRequest.setCreated(LocalDateTime.MAX);
        itemRequest.setRequestor(new User());
        itemRequest.setDescription("desc");

        itemRequestDto.setDescription("desc");

        user.setId(1L);
        user.setEmail("nnn@memes.ea");
        user.setName("amogus");
    }

    @Test
    void mapToItemRequest() {
        ItemRequest itemRequest1 = ItemRequestMapper.mapToItemRequest(itemRequestDto, user);
        assertEquals(itemRequestDto.getDescription(), itemRequest1.getDescription());
    }

    @Test
    void mapToDto() {
        ItemRequestDto itemRequestDto1 = ItemRequestMapper.mapToDto(itemRequest);
        assertEquals(itemRequest.getId(), itemRequestDto1.getId());
    }
}