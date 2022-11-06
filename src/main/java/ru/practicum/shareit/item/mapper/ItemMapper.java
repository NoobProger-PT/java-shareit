package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(ItemDto itemDto, long userId) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setOwnerId(userId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setOwnerId(item.getOwnerId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        return itemDto;
    }

    public static ItemWithBookingDto mapToItemWithBookingDto(ItemDto i, BookingDtoShort b1, BookingDtoShort b2) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(i.getId());
        itemWithBookingDto.setName(i.getName());
        itemWithBookingDto.setDescription(i.getDescription());
        itemWithBookingDto.setAvailable(i.getAvailable());
        itemWithBookingDto.setLastBooking(b1);
        itemWithBookingDto.setNextBooking(b2);
        return itemWithBookingDto;
    }

    public static ItemWithBookingDto mapToItemWithBookingDto(ItemDto i) {
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
        itemWithBookingDto.setId(i.getId());
        itemWithBookingDto.setName(i.getName());
        itemWithBookingDto.setDescription(i.getDescription());
        itemWithBookingDto.setAvailable(i.getAvailable());
        return itemWithBookingDto;
    }

    public static ItemWithBookingAndCommentDto mapToItemBookingCommentDto(ItemWithBookingDto i, List<CommentDto> c) {
        ItemWithBookingAndCommentDto item = new ItemWithBookingAndCommentDto();
        item.setId(i.getId());
        item.setName(i.getName());
        item.setDescription(i.getDescription());
        item.setAvailable(i.getAvailable());
        item.setLastBooking(i.getLastBooking());
        item.setNextBooking(i.getNextBooking());
        item.setComments(c);
        return item;
    }
}
