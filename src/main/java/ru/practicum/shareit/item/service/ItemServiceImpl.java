package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    public List<ItemDto> getItems(long userId) {
        return new ArrayList<>(repository.findAll(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList()));
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        itemDto.setUserId(userId);
        Item item = ItemMapper.mapToItem(itemDto, userId);
        ItemDto dto = ItemMapper.mapToItemDto(repository.save(item));
        return dto;
    }

    public ItemDto findItemById(long itemId) {
        ItemDto itemDto = ItemMapper.mapToItemDto(repository.findItemById(itemId));
        return itemDto;
    }

    public List<ItemDto> findItemByText(String text) {
        String lowerCaseText;
        if (text.length() != 0) {
            lowerCaseText = text.toLowerCase();
        } else {
            return new ArrayList<>();
        }
        return new ArrayList<>(repository.findItemByName(lowerCaseText).stream()
                .map(ItemMapper::mapToItemDto).collect(Collectors.toList()));
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);
        itemDto.setUserId(userId);
        ItemDto dto = ItemMapper.mapToItemDto(repository.update(userId, itemId, itemDto));
        return dto;
    }

    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndItemId(userId, itemId);
    }
}
