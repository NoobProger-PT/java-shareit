package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(String name, String description, boolean b);

    List<Item> findAllByRequestIdIn(List<Long> id);

    Optional<Item> findAllByRequestId(long id);

}