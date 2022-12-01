package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get Items");
        return itemClient.get(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Positive @PathVariable long itemId) {
        log.info("Get by id");
        return itemClient.getById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam String text,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get by text");
        return itemClient.getByText(text, from, size, userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Validated({Marker.Create.class}) @RequestBody ItemDto itemDto) {
        log.info("Post item");
        return itemClient.add(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Positive @PathVariable long itemId,
                                             @Validated({Marker.Create.class}) @RequestBody CommentDto commentDto) {
        log.info("Post item's comment");
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Positive @PathVariable long itemId,
                                         @Validated({Marker.Update.class}) @RequestBody ItemDto itemDto) {
        log.info("Update Item");
        return itemClient.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Positive @PathVariable long itemId) {
        log.info("Delete Item");
        return itemClient.delete(userId, itemId);
    }
}
