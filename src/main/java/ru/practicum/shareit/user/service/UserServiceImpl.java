package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new UserDontExistsException("Пользователь не найден"));
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        User user = repository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    public UserDto update(long id, UserDto userDto) {
        User updatedUser = repository.findById(id).orElseThrow(() ->
                new UserDontExistsException("Пользователь не найден"));
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Transactional
    public void delete(long userId) {
        repository.deleteById(userId);
    }
}