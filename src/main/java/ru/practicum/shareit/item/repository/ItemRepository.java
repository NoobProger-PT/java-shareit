package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final Map<Long, Item> storage = new LinkedHashMap<>();

    private long id = 0;

    private final UserRepository userRepository;

    public List<Item> findAll(long userId) {
        return items.get(userId).stream().collect(Collectors.toList());
    }

    public Item findItemById(long itemId) {
        if (!storage.containsKey(itemId)) {
            throw new ItemDontExistsException("Предмет не найден.");
        }
        Item item = storage.get(itemId);
        return item;
    }

    public List<Item> findItemByName(String text) {
        List<Item> listOfItems;
        Set<Item> result = new TreeSet<>((o1, o2) -> {
            long i = o1.getId() - o2.getId();
            return (int) i;
        });
        List<Item> findByText = storage.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(text)
                        || i.getDescription().toLowerCase().contains(text))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
        result.addAll(findByText);
        listOfItems = result.stream().collect(Collectors.toList());
        return listOfItems;
    }

    public Item save(Item item) {
        userRepository.findById(item.getUserId());
        items.compute(item.getUserId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            item.setId(++id);
            userItems.add(item);
            return userItems;
        });
        storage.put(item.getId(), item);
        return item;

    }

    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
        if (storage.containsKey(itemId)) {
            storage.remove(itemId);
        }
    }

    public Item update(long userId, long itemId, ItemDto itemDto) {
        List<Item> userItem;
        Item updatedItem;

        if (items.containsKey(userId)) {
            userItem = items.get(userId);
        } else {
            throw new UserDontExistsException("Пользователя с ID " + userId + " нет.");
        }

        userItem = items.get(userId);

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
        return updatedItem;
    }
}

