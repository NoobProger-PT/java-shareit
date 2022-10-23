package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidItem;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository repository;

    public List<ItemDto> getItems(long userId) {
        return repository.findAll(userId);
    }

    public ItemDto addNewItem(long userId, Item item) {
        if (item.getAvailable() == null) {
            throw new InvalidItem("Укажите статус вещи.");
        }
        item.setUserId(userId);
        ItemDto itemDto = repository.save(item);
        return itemDto;
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
