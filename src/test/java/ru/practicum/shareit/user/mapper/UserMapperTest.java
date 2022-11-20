package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private static final UserDto dto = new UserDto();
    private static final User user = new User();

    @BeforeAll
    public static void createShortDto() {
        dto.setId(1L);
        dto.setName("name");
        dto.setEmail("m@m.m");

        user.setId(1L);
        user.setName("name");
        user.setEmail("m@m.m");
    }

    @Test
    void mapToUser() {
        User user1 = UserMapper.mapToUser(dto);
        assertEquals(dto.getEmail(), user1.getEmail());
    }

    @Test
    void mapToUserDto() {
        UserDto userDto = UserMapper.mapToUserDto(user);
        assertEquals(user.getEmail(), userDto.getEmail());
    }
}