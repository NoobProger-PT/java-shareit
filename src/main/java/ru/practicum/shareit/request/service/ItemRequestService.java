package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(long userId, InputItemRequestDto dto);
    ItemRequestDto getById(long requestId, long userId);
    List<ItemRequestDto> getAll(long userId);
    List<ItemRequestDto> getByFromAndSize(int from, int size, long userId);
}
