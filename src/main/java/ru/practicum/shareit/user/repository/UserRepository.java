package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.InvalidUser;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    private long id = 0;

    public List<User> findAll() {
        return users;
    }

    public User findById(long id) {
        List<User> userById = users.stream()
                .filter(u -> u.getId() == id)
                .collect(Collectors.toList());
        if (userById.size() == 0) {
            throw new UserDontExistsException("Пользователь не найден.");
        }
        return userById.get(0);
    }

    public User save(User user) {
        if (users.contains(user)) {
            throw new InvalidUser("Пользователь с таким Email уже существует.");
        }
        if (id == 0) {
            id = getId();
        }
        user.setId(id++);
        users.add(user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.contains(user)) {
            throw new UserDontExistsException("Пользователь не найден в базе данных.");
        }
        int userPosition = users.indexOf(user);
        User updatedUser = users.get(userPosition);

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            List<User> checkEmail = users.stream()
                    .filter(u -> u.getEmail().equals(user.getEmail()))
                    .collect(Collectors.toList());
            if (checkEmail.size() == 0) {
                updatedUser.setEmail(user.getEmail());
            } else {
                throw new InvalidUser("Почтовый адрес уже занят.");
            }
        }
        return updatedUser;
    }

    public void deleteUser(long userId) {
        List<User> userForDelete = users.stream()
                .filter(u -> u.getId() == userId)
                .collect(Collectors.toList());
        if (userForDelete.size() == 0) {
            throw new UserDontExistsException("Пользователь не найден.");
        } else {
            users.remove(userForDelete.get(0));
        }
    }

    private long getId() {
        long lastId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
