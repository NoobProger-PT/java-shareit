package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    Item item = new Item();
    ItemRequest itemRequest = new ItemRequest();
    User user = new User();

    @BeforeEach
    public void createData() {
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@mail.ru");
        userRepository.save(user);

        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemRepository.save(item);
    }

    @Test
    void findAllByRequestorId() {
        List<ItemRequest> requests = itemRequestRepository
                .findAllByRequestorId(user.getId(), Sort.by(Sort.Direction.DESC, "id"));
        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0).getRequestor().getName(), equalTo(user.getName()));
    }

    @Test
    void findAll() {
        List<ItemRequest> requests = itemRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0).getRequestor().getName(), equalTo(user.getName()));
    }
}