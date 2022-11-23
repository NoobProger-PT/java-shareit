package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    private UserServiceImpl userService;

    private UserRepository userRepository;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));

        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        var result = userService.getAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.get(0).getId());
    }

    @Test
    void getById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        var result = userService.getById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getName(), result.getName());
    }

    @Test
    void dontGetByWrongId() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            userService.getById(10L);
        });
    }

    @Test
    void save() {
        when(userRepository.save(any())).thenReturn(user);
        var result = userService.save(new UserDto());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getName(), result.getName());
    }

    @Test
    void update() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("mulo@m.ru");
        userDto.setName("user");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        var result = userService.update(1L, userDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getName(), result.getName());
    }

    @Test
    void dontUpdateWithWrongId() {
        Assertions.assertThrows(UserDontExistsException.class, () -> {
            userService.update(10L, new UserDto());
        });
    }

    @Test
    void delete() {
        var userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        userService.delete(userId);
        verify(userRepository, times(0)).delete(any());
    }
}