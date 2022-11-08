package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDto> getAll(long userId);

    ItemDto addNew(long userId, ItemDto itemDto);

    ItemWithBookingAndCommentDto findById(long itemId, long userId);

    List<ItemDto> findByText(String text);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    void delete(long userId, long itemId);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
