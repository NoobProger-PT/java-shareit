package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public List<UserDto> getAllUsers() {
        return new ArrayList<>(repository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList()));
    }

    public UserDto getById(long id) {
        User user = repository.findById(id).get();
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto saveUser(UserDto userDto) {
        User user = repository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto updateUser(long id, UserDto userDto) {
        User updatedUser = repository.findById(id).get();
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }
        User toDto = repository.save(updatedUser);
        return UserMapper.mapToUserDto(toDto);
    }

    @Transactional
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }
}