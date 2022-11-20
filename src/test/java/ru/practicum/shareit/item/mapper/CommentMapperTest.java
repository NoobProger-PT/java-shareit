package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private static final CommentDto dto = new CommentDto();
    private static final Comment comment = new Comment();

    private static final User user = new User();
    private static final Item item = new Item();

    private static final ItemRequest itemRequest = new ItemRequest();

    @BeforeAll
    public static void create() {
        user.setId(1L);
        user.setEmail("nnn@memes.ea");
        user.setName("amogus");

        itemRequest.setId(20L);
        itemRequest.setCreated(LocalDateTime.MAX);
        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");

        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(user);
        item.setDescription("desc");
        item.setRequest(itemRequest);
        item.setName("name");

        dto.setId(1L);
        dto.setAuthorName("name");
        dto.setText("text");
        dto.setCreated(LocalDateTime.MIN);

        comment.setUser(new User());
        comment.setItem(new Item());
        comment.setCreated(LocalDateTime.MIN);
        comment.setText("text1");
        comment.setId(20L);
    }

    @Test
    void mapToComment() {
        Comment comment1 = CommentMapper.mapToComment(dto, 20L, 1L);
        assertEquals(dto.getId(), comment1.getId());
    }

    @Test
    void mapToDto() {
        CommentDto dto1 = CommentMapper.mapToDto(comment);
        assertEquals(comment.getId(), dto1.getId());
    }
}