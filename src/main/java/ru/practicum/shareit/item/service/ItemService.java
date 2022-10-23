package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long userId, Item item);

    ItemDto findItemById(long itemId);

    List<ItemDto> findItemByText(String text);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);
}
