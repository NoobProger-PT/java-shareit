package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {

    public static Item mapToItem(ItemDto itemDto, long userId) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUserId(userId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getUserId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
