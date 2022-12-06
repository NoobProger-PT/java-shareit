package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private final User user = new User();

    @Test
    public void shouldGet() {
        createUser();
        List<User> users = userRepository.findAll();
        assertThat(users.get(0).getName(), equalTo(user.getName()));
    }

    @Test
    public void shouldGetById() {
        createUser();
        User userById = userRepository.findById(1L).get();
        assertThat(userById.getId(), equalTo(user.getId()));
    }

    @Test
    public void shouldSave() {
        createUser();
        User saveUser = userRepository.save(user);
        assertThat(saveUser.getId(), equalTo(user.getId()));
    }

    @Test
    public void shouldUpdate() {
        createUser();
        User updateUser = userRepository.save(user);
        assertThat(updateUser.getId(), equalTo(user.getId()));
    }

    private void createUser() {
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@mail.ru");
        userRepository.save(user);
    }
}