package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.InvalidUser;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqSet = new HashSet<>();

    private long id = 0;

    public List<UserDto> findAll() {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : users.values()) {
            dtoList.add(UserMapper.mapToUserDto(user));
        }
        return dtoList;
    }

    public User findById(long id) {
        if (!users.keySet().contains(id)) {
            throw new UserDontExistsException("Пользователь не найден.");
        }
        User userById = users.get(id);
        return userById;
    }

    public User save(User user) {
        checkEmail(user.getEmail());
        user.setId(++id);
        users.put(user.getId(), user);
        emailUniqSet.add(user.getEmail());
        return user;
    }

    public User updateUser(User user) {
        if (!users.keySet().contains(id)) {
            throw new UserDontExistsException("Пользователь не найден в базе данных.");
        }

        User updatedUser = users.get(user.getId());

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            emailUniqSet.remove(updatedUser.getEmail());
            updatedUser.setEmail(user.getEmail());
            emailUniqSet.add(user.getEmail());
        }
        return updatedUser;
    }

    public void deleteUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new UserDontExistsException("Пользователь не найден.");
        } else {
            User userForDelete = users.get(userId);
            emailUniqSet.remove(userForDelete.getEmail());
            users.remove(userId);
        }
    }

    private void checkEmail(String email) {
        if (emailUniqSet.contains(email)) {
            throw new InvalidUser("Пользователь с таким Email уже существует.");
        }
    }
}
