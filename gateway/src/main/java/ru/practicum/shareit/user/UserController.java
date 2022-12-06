package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@Positive @PathVariable long userId) {
        log.info("Get user by ID");
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Validated({Marker.Create.class}) @RequestBody UserDto userDto) {
        log.info("Post user");
        return userClient.add(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated({Marker.Update.class}) @RequestBody UserDto userDto,
                                         @Positive @PathVariable long userId) {
        log.info("Update user");
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@Positive @PathVariable long userId) {
        log.info("Delete user");
        return userClient.delete(userId);
    }
}
