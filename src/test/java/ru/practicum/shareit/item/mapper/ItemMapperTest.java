package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private static final CommentDto commentDtoto = new CommentDto();
    private static final Comment comment = new Comment();
    private static final User user = new User();
    private static final UserDto userDto = new UserDto();
    private static final Item item = new Item();
    private static final ItemDto itemDto = new ItemDto();
    private static final BookingDtoShort bookingDtoShort = new BookingDtoShort();
    private static final BookingDtoShort bookingDtoShort1 = new BookingDtoShort();
    private static final ItemRequest itemRequest = new ItemRequest();

    @BeforeAll
    public static void create() {
        user.setId(1L);
        user.setEmail("nnn@memes.ea");
        user.setName("amogus");

        userDto.setId(1L);
        userDto.setEmail("nnn@memes.ea");
        userDto.setName("amogus");

        itemRequest.setId(20L);
        itemRequest.setCreated(LocalDateTime.MAX);
        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");

        itemDto.setId(20L);
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);
        itemDto.setDescription("desc");
        itemDto.setName("name");
        itemDto.setRequestId(50L);

        item.setId(20L);
        item.setAvailable(true);
        item.setOwner(user);
        item.setDescription("desc");
        item.setRequest(itemRequest);
        item.setName("name");

        commentDtoto.setId(1L);
        commentDtoto.setAuthorName("name");
        commentDtoto.setText("text");
        commentDtoto.setCreated(LocalDateTime.MIN);

        comment.setUser(new User());
        comment.setItem(new Item());
        comment.setCreated(LocalDateTime.MIN);
        comment.setText("text1");
        comment.setId(20L);

        bookingDtoShort.setId(1L);
        bookingDtoShort.setBookerId(1L);

        bookingDtoShort.setId(2L);
        bookingDtoShort.setBookerId(2L);
    }

    @Test
    void mapToItem() {
        Item item1 = ItemMapper.mapToItem(itemDto, 1L);
        assertEquals(itemDto.getName(), item1.getName());
    }

    @Test
    void mapToItemDto() {
        ItemDto itemDto1 = ItemMapper.mapToItemDto(item);
        assertEquals(item.getName(), itemDto1.getName());
    }

    @Test
    void mapToItemWithBookingDto() {
        ItemWithBookingDto itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto,
                bookingDtoShort1, bookingDtoShort);
        assertEquals(itemDto.getName(), itemWithBookingDto.getName());
        assertEquals(bookingDtoShort.getBookerId(), itemWithBookingDto.getNextBooking().getBookerId());
    }

    @Test
    void testMapToItemWithBookingDto() {
        ItemWithBookingDto itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto);
        assertEquals(itemDto.getName(), itemWithBookingDto.getName());
    }

    @Test
    void mapToItemBookingCommentDto() {
        ItemWithBookingDto itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto,
                bookingDtoShort1, bookingDtoShort);
        ItemWithBookingAndCommentDto item1 = ItemMapper.mapToItemBookingCommentDto(itemWithBookingDto,
                List.of(commentDtoto));
        assertEquals(itemWithBookingDto.getId(), item1.getId());
    }
}