package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public List<UserDto> getAllUsers() {
        return new ArrayList<>(repository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList()));
    }

    public UserDto getById(long id) {
        User user = repository.findById(id);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto saveUser(UserDto userDto) {

        User user = repository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(long id, UserDto userDto) {
        userDto.setId(id);
        User updatedUser = UserMapper.mapToUser(userDto);
        User toDto = repository.updateUser(updatedUser);
        return UserMapper.mapToUserDto(toDto);
    }

    public void deleteUser(long userId) {
        repository.deleteUser(userId);
    }
}
