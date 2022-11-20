package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private UserDto userDto = new UserDto();

    @BeforeEach
    public void createData() {
        userDto.setName("name");
        userDto.setEmail("mail@mail.mail");
        userService.save(userDto);
    }

    @Test
    void getAll() {
        List<UserDto> users = userService.getAll();
        assertThat(users.size(), equalTo(1));
    }

    @Test
    void getById() {
        UserDto userById = userService.getById(1L);
        assertThat(userById.getName(), equalTo(userDto.getName()));
    }

    @Test
    void dontGetByWrongId() {
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        userService.getById(10L);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с юзером");
                    }
                });

        assertEquals("ошибка с юзером", exception.getMessage());
    }

    @Test
    void save() {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("newName");
        newUserDto.setEmail("new@new.new");
        userService.save(newUserDto);
        UserDto dto = userService.getById(2L);
        assertThat(dto.getName(), equalTo(newUserDto.getName()));
    }

    @Test
    void update() {
        UserDto updated = new UserDto();
        updated.setName("updated");
        updated.setEmail("updated@up.pu");
        UserDto dto = userService.update(1L, updated);
        assertThat(dto.getName(), equalTo(updated.getName()));
    }

    @Test
    void dontUpdateWithWrongId() {
        UserDto updated = new UserDto();
        updated.setName("updated");
        final UserDontExistsException exception = assertThrows(
                UserDontExistsException.class,
                () -> {
                    try {
                        userService.update(10L, updated);
                    } catch (UserDontExistsException e) {
                        throw new UserDontExistsException("ошибка с юзером");
                    }
                });

        assertEquals("ошибка с юзером", exception.getMessage());
    }

    @Test
    void delete() {
        userService.delete(1L);
    }
}