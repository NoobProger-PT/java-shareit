package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    Page<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(String name, String description, boolean b);

    Page<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(String name, String description, boolean b,
                                                                          Pageable pageable);

    List<Item> findAllByRequestIdIn(List<Long> id);

    Optional<Item> findAllByRequestId(long id);
}