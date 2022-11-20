package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoTest {

    private static final ItemRequestDto itemRequest = new ItemRequestDto();
    private static final ItemRequestDto itemRequest1 = new ItemRequestDto();

    @BeforeAll
    public static void createShortDto() {
        itemRequest.setId(20L);
        itemRequest.setCreated(LocalDateTime.MAX);
        itemRequest.setDescription("desc");
        itemRequest.setItems(new ArrayList<>());
    }

    @Test
    void addItemInList() {
        ItemDto itemDto = new ItemDto();
        assertEquals(0, itemRequest.getItems().size());
        itemRequest.addItemInList(itemDto);
        assertEquals(1, itemRequest.getItems().size());
    }

    @Test
    void getId() {
        assertEquals(20L, itemRequest.getId());
    }

    @Test
    void getDescription() {
        assertEquals("desc", itemRequest.getDescription());
    }

    @Test
    void getCreated() {
        assertEquals(LocalDateTime.MAX, itemRequest.getCreated());
    }

    @Test
    void getItems() {
        assertNotNull(itemRequest.getItems().get(0));
    }

    @Test
    void setId() {
        itemRequest1.setId(2L);
        assertEquals(2L, itemRequest1.getId());
    }

    @Test
    void setDescription() {
        itemRequest1.setDescription("desc");
        assertEquals("desc", itemRequest1.getDescription());
    }

    @Test
    void setCreated() {
        itemRequest1.setCreated(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, itemRequest1.getCreated());
    }

    @Test
    void setItems() {
        itemRequest1.setItems(List.of(new ItemDto()));
        assertNotNull(itemRequest1.getItems().get(0));
    }
}