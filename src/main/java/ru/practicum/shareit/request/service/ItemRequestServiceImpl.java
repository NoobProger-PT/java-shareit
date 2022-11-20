package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(long userId, InputItemRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDontExistsException("Такого пользователя нет"));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(dto, user));
        return ItemRequestMapper.mapToDto(itemRequest);
    }

    @Override
    public ItemRequestDto getById(long requestId, long userId) {
        checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemDontExistsException("Такого реквеста нет"));
        Item item = itemRepository.findAllByRequestId(requestId).orElseThrow(() ->
                new ItemDontExistsException("Такого предмета по реквесту нет"));
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        ItemRequestDto result = ItemRequestMapper.mapToDto(itemRequest);
        result.addItemInList(itemDto);
        return result;
    }

    @Override
    public List<ItemRequestDto> getAll(long userId) {
        checkUser(userId);

        Map<Long, List<ItemRequestDto>> itemRequestsMap = itemRequestRepository
                .findAllByRequestorId(userId, Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(ItemRequestMapper::mapToDto)
                .collect(groupingBy(ItemRequestDto::getId));
        List<Long> itemRequestId = new ArrayList<>();
        for (Long id : itemRequestsMap.keySet()) {
            itemRequestId.add(id);
        }
        Map<Long, List<ItemDto>> ItemMap = itemRepository
                .findAllByRequestIdIn(itemRequestId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(groupingBy(ItemDto::getRequestId));
        List<ItemRequestDto> result = new ArrayList<>();
        for (Long id : itemRequestsMap.keySet()) {
            if (ItemMap.containsKey(id)) {
                itemRequestsMap.get(id).get(0).addItemInList(ItemMap.get(id).get(0));
            }
            result.add(itemRequestsMap.get(id).get(0));
        }
        return result;
    }

    @Override
    public List<ItemRequestDto> getByFromAndSize(int from, int size, long userId) {
        Map<Long, List<ItemRequestDto>> itemRequestsMap = itemRequestRepository
                .findAll(Sort.by(Sort.Direction.DESC, "created")).stream()
                .filter(i -> i.getRequestor().getId() != userId)
                .map(ItemRequestMapper::mapToDto)
                .collect(groupingBy(ItemRequestDto::getId));
        List<Long> itemRequestId = new ArrayList<>(itemRequestsMap.keySet());
        Map<Long, List<ItemDto>> ItemMap = itemRepository
                .findAllByRequestIdIn(itemRequestId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(groupingBy(ItemDto::getRequestId));
        List<ItemRequestDto> ItemRequestlist = new ArrayList<>();
        for (Long id : itemRequestsMap.keySet()) {
            if (ItemMap.containsKey(id)) {
                itemRequestsMap.get(id).get(0).addItemInList(ItemMap.get(id).get(0));
            }
            ItemRequestlist.add(itemRequestsMap.get(id).get(0));
        }
        List<ItemRequestDto> result = new ArrayList<>();
        for (int i = from; i <= size; i++) {
            if (i >= ItemRequestlist.size()) {
                break;
            }
            result.add(ItemRequestlist.get(i));
        }
        return result;
    }

    private void checkUser(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UserDontExistsException("нет такого юзера."));
    }
}
