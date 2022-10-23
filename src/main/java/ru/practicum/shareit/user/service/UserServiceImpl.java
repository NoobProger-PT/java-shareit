package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public UserDto getById(long id) {
        User user = repository.findById(id);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto saveUser(UserDto userDto) {
        User user = repository.save(UserMapper.mapToUser(userDto));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(long id, User user) {
        user.setId(id);
        User updatedUser = repository.updateUser(user);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public void deleteUser(long userId) {
        repository.deleteUser(userId);
    }
}
