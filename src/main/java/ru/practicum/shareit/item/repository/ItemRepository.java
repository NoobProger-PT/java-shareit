package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();

    private long id = 0;

    private final UserRepository userRepository;

    public List<ItemDto> findAll(long userId) {
        List<Item> userItems = items.get(userId);
        List<ItemDto> items = new ArrayList<>();
        for (Item item : userItems) {
            items.add(ItemMapper.mapToItemDto(item));
        }
        return items;
    }

    public ItemDto findItemById(long itemId) {
        List<Item> itemById = new ArrayList<>();
        for (List<Item> itemList : items.values()) {
            itemById = itemList.stream()
                    .filter(i -> i.getId() == itemId)
                    .collect(Collectors.toList());
        }
        if (itemById.size() == 0) {
            throw new ItemDontExistsException("Предмет не найден.");
        }
        ItemDto dto = ItemMapper.mapToItemDto(itemById.get(0));
        return dto;
    }

    public List<ItemDto> findItemByName(String text) {
        Set<Item> result = new TreeSet<>((o1, o2) -> {
            long i = o1.getId() - o2.getId();
            return (int) i;
        });
        List<ItemDto> setOfItems = new ArrayList<>();
        List<Item> item1;
        List<Item> item2;
        for (List<Item> itemList : items.values()) {
            item1 = itemList.stream()
                    .filter(i -> i.getName().toLowerCase().contains(text))
                    .filter(i -> i.getAvailable() == true)
                    .collect(Collectors.toList());
            result.addAll(item1);
            item2 = itemList.stream()
                    .filter(i -> i.getDescription().toLowerCase().contains(text))
                    .filter(i -> i.getAvailable() == true)
                    .collect(Collectors.toList());
            result.addAll(item2);
        }
        for (Item item : result) {
            setOfItems.add(ItemMapper.mapToItemDto(item));
        }
        return setOfItems;
    }

    public ItemDto save(Item item) {
        if (id == 0) {
            id = getId();
        }
        if (userRepository.findById(item.getUserId()).getId() == item.getUserId()) {
            items.compute(item.getUserId(), (userId, userItems) -> {
                if (userItems == null) {
                    userItems = new ArrayList<>();
                }
                item.setId(id++);
                userItems.add(item);
                return userItems;
            });
            ItemDto dto = ItemMapper.mapToItemDto(item);
            return dto;
        } else {
            throw new UserDontExistsException("Пользователя с ID " + item.getUserId() + " не существует");
        }
    }

    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        List<Item> userItem;
        Item updatedItem;

        if (items.containsKey(userId)) {
            userItem = items.get(userId);
        } else {
            throw new UserDontExistsException("Пользователя с ID " + userId + " нет.");
        }

        userItem = userItem.stream()
                .filter(i -> i.getId() == itemId)
                .collect(Collectors.toList());

        if (userItem.size() != 0) {
            updatedItem = userItem.get(0);
        } else {
            throw new ItemDontExistsException("Предмет не найден.");
        }

        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        ItemDto dto = ItemMapper.mapToItemDto(updatedItem);
        return dto;
    }

    private long getId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}

