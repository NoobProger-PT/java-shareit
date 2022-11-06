package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDto> getItems(long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemWithBookingAndCommentDto findItemById(long itemId, long userId);

    List<ItemDto> findItemByText(String text);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
