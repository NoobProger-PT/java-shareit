package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto();
    private final ItemWithBookingAndCommentDto itemWithBookingAndCommentDto = new ItemWithBookingAndCommentDto();
    private final ItemDto itemDto = new ItemDto();
    private final BookingDtoShort lastBooking = new BookingDtoShort();
    private final BookingDtoShort nextBooking = new BookingDtoShort();
    private final CommentDto commentDto = new CommentDto();

    @BeforeEach
    public void createItemAndCommentDto() {
        lastBooking.setBookerId(1L);
        lastBooking.setId(1L);

        nextBooking.setId(2L);
        nextBooking.setBookerId(2L);

        itemWithBookingDto.setId(21L);
        itemWithBookingDto.setName("name");
        itemWithBookingDto.setDescription("desc");
        itemWithBookingDto.setAvailable(true);
        itemWithBookingDto.setLastBooking(lastBooking);
        itemWithBookingDto.setNextBooking(nextBooking);

        commentDto.setId(2L);
        commentDto.setText("comment");
        commentDto.setAuthorName("author");
        commentDto.setCreated(LocalDateTime.now());

        itemWithBookingAndCommentDto.setId(65L);
        itemWithBookingAndCommentDto.setName("another item name");
        itemWithBookingAndCommentDto.setDescription("another desc");
        itemWithBookingAndCommentDto.setAvailable(true);
        itemWithBookingAndCommentDto.setLastBooking(lastBooking);
        itemWithBookingAndCommentDto.setNextBooking(nextBooking);
        itemWithBookingAndCommentDto.setComments(List.of(commentDto));

        itemDto.setId(55L);
        itemDto.setOwner(new UserDto(20L));
        itemDto.setName("item name");
        itemDto.setDescription("item desc");
        itemDto.setRequestId(11L);
        itemDto.setAvailable(true);
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(itemService.getAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemWithBookingDto));

        mvc.perform(get("/items?from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithBookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(itemWithBookingDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemWithBookingDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemWithBookingDto.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(itemWithBookingDto
                        .getLastBooking().getBookerId().intValue())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(itemWithBookingDto
                        .getNextBooking().getBookerId().intValue())));
    }

    @Test
    public void shouldGetById() throws Exception {
        when(itemService.findById(anyLong(), anyLong()))
                .thenReturn(itemWithBookingAndCommentDto);

        mvc.perform(get("/items/65")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingAndCommentDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemWithBookingAndCommentDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithBookingAndCommentDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithBookingAndCommentDto.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(itemWithBookingAndCommentDto
                        .getLastBooking().getBookerId().intValue())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(itemWithBookingAndCommentDto
                        .getNextBooking().getBookerId().intValue())))
                .andExpect(jsonPath("$.comments[0].id", is(itemWithBookingAndCommentDto
                        .getComments().get(0).getId().intValue())));
    }

    @Test
    public void shouldGetAllByText() throws Exception {
        when(itemService.findByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text='desc'&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$[0].owner.id", is(itemDto.getOwner().getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId().intValue())));
    }

    @Test
    public void shouldGetAllByWrongText() throws Exception {
        when(itemService.findByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text=&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @Test
    public void shouldAdd() throws Exception {
        when(itemService.addNew(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId().intValue())));
    }

    @Test
    public void shouldAddComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/55/comment")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId().intValue())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    public void shouldUpdate() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/55")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId().intValue())));
    }

    @Test
    public void shouldDelete() throws Exception {
        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 20L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}