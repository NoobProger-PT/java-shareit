package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    private UserService service;

    @Test
    void addUser() {
        UserDto user = createUser();
        service.save(user);
        assertEquals(user.getName(), service.getById(1L).getName());
    }

    @Test
    void updateUser() {
        UserDto user = createUser();
        service.save(user);
        user.setEmail("emailnew@mail.ru");
        user.setName("new");
        user.setId(1L);
        service.update(1L, user);
        assertEquals("new", service.getById(1L).getName());
    }

    @Test
    void updateUserWithWrongId() {
        UserDto user = createUser();
        service.save(user);
        user.setEmail("emailnew@mail.ru");
        user.setName("new");
        user.setId(1L);
        assertThrows(UserDontExistsException.class, () -> service.update(10L, user));
    }

    @Test
    void delete() {
        UserDto user = createUser();
        service.save(user);
        assertEquals(user.getName(), service.getById(1L).getName());
        service.delete(1L);
        assertThrows(UserDontExistsException.class, () -> service.getById(1L));
    }

    @Test
    void getAll() {
        UserDto user = createUser();
        service.save(user);
        assertEquals(user.getName(), service.getAll().get(0).getName());
    }

    private UserDto createUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.ru");
        return userDto;
    }
/*
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
    }*/
}