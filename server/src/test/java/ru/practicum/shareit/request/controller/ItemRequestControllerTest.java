package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.InputItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private final InputItemRequestDto inputDto = new InputItemRequestDto();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();

    @BeforeEach
    public void createItemAndCommentDto() {
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("desc");
        itemRequestDto.setItems(List.of(new ItemDto(1L)));
        itemRequestDto.setCreated(LocalDateTime.now());

        inputDto.setDescription("desc");
    }

    @Test
    public void getAll() throws Exception {
        when(itemRequestService.getAll(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId().intValue())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemRequestDto.getItems()
                        .get(0).getId().intValue())));
    }

    @Test
    public void getById() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(itemRequestDto.getItems()
                        .get(0).getId().intValue())));
    }

    @Test
    public void getAllFrom() throws Exception {
        when(itemRequestService.getByFromAndSize(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all?from=0&size=1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId().intValue())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemRequestDto.getItems()
                        .get(0).getId().intValue())));
    }

    @Test
    public void create() throws Exception {
        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(itemRequestDto.getItems()
                        .get(0).getId().intValue())));
    }
}