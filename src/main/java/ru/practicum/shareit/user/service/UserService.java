package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getById(long id);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long userId);
}
