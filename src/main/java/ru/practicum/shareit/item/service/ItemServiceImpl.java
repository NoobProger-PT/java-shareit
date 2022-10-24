package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    public List<ItemDto> getItems(long userId) {
        return repository.findAll(userId);
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        itemDto.setUserId(userId);
        Item item = ItemMapper.mapToItem(itemDto, userId);
        ItemDto dto = repository.save(item);
        return dto;
    }

    public ItemDto findItemById(long itemId) {
        ItemDto itemDto = repository.findItemById(itemId);
        return itemDto;
    }

    public List<ItemDto> findItemByText(String text) {
        String lowerCaseText;
        if (text.length() != 0) {
            lowerCaseText = text.toLowerCase();
        } else {
            return new ArrayList<>();
        }
        return repository.findItemByName(lowerCaseText);
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);
        itemDto.setUserId(userId);
        ItemDto dto = repository.update(userId, itemId, itemDto);
        return dto;
    }

    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndItemId(userId, itemId);
    }
}
