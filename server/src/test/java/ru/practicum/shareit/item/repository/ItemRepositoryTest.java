package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private final User user = new User();
    private final ItemRequest itemRequest = new ItemRequest();
    private final Item item = new Item();

    @Test
    public void shouldFindByOwnerId() {
        createUserAndItem();
        List<Item> userItems = itemRepository.findAllByOwnerId(user.getId());
        assertThat(userItems.size(), equalTo(1));
        assertThat(userItems.get(0).getOwner().getId(), equalTo(user.getId()));
    }

    @Test
    public void shouldNotFindByOwnerId() {
        createUserAndItem();
        List<Item> userItems = itemRepository.findAllByOwnerId(100L);
        assertThat(userItems.size(), equalTo(0));
    }

    @Test
    public void shouldFindItemByNameOrDesc() {
        createUserAndItem();
        List<Item> items = itemRepository
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable("name", "name", true);
        assertThat(items.size(), equalTo(1));
    }

    @Test
    public void shouldNotFindItemByNameOrDesc() {
        createUserAndItem();
        List<Item> items = itemRepository
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable("error", "error", true);
        assertThat(items.size(), equalTo(0));
    }

    @Test
    public void shouldFindByRequestId() {
        createUserAndItem();
        List<Item> items = itemRepository
                .findAllByRequestIdIn(new ArrayList<>(List.of(user.getId())));
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getRequest().getId(), equalTo(user.getId()));
    }

    @Test
    public void shouldNotFindByRequestId() {
        createUserAndItem();
        List<Item> items = itemRepository
                .findAllByRequestIdIn(new ArrayList<>(List.of(100L)));
        assertThat(items.size(), equalTo(0));
    }

    private void createUserAndItem() {
        user.setName("name");
        user.setEmail("email@mail.ru");
        userRepository.save(user);

        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemRepository.save(item);
    }
}