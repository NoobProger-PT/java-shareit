package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemDontExistsException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.UserDontExistsException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public List<ItemWithBookingDto> getItems(long userId) {
        List<ItemWithBookingDto> items = itemRepository.findAllByOwnerId(userId).stream()
                .map(i -> {
                    BookingDtoShort lastBook = BookingMapper.mapToShortDto(
                            bookingRepository.findByItemIdAndEndDateBeforeOrderByEndDateDesc(
                                    i.getId(), LocalDateTime.now()));
                    BookingDtoShort nextBook = BookingMapper.mapToShortDto(
                            bookingRepository.findByItemIdAndStartDateAfterOrderByEndDateAsc(
                                    i.getId(), LocalDateTime.now()));
                    ItemWithBookingDto itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(
                            ItemMapper.mapToItemDto(i), lastBook, nextBook);
                    return itemWithBookingDto;
                })
                .collect(Collectors.toList());
        return items;
    }

    @Transactional
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователя с id " + userId + " не существует."));
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, userId));
        return ItemMapper.mapToItemDto(item);
    }

    public ItemWithBookingAndCommentDto findItemById(long itemId, long userId) {
        ItemWithBookingDto itemWithBookingDto;
        ItemDto itemDto = ItemMapper.mapToItemDto(itemRepository.findById(itemId).get());
        BookingDtoShort lastBook = BookingMapper.mapToShortDto(
                bookingRepository.findByItemIdAndEndDateBeforeOrderByEndDateDesc(itemId, LocalDateTime.now()));
        BookingDtoShort nextBook = BookingMapper.mapToShortDto(
                bookingRepository.findByItemIdAndStartDateAfterOrderByEndDateAsc(itemId, LocalDateTime.now()));
        if (itemDto.getOwnerId() == userId) {
            itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto, lastBook, nextBook);
        } else {
            itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto);
        }
        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
        return ItemMapper.mapToItemBookingCommentDto(itemWithBookingDto, comments);
    }

    public List<ItemDto> findItemByText(String text) {
        if (text.length() == 0) {
            return new ArrayList<>();
        }
        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(text, text).stream()
                .filter(i -> i.isAvailable())
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(userId);
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemDontExistsException("Вещи с таким id нет"));
        if (updateItem.getOwnerId() != userId) {
            throw new UserDontExistsException("У пользователя с id " + userId + " вещи с id " + itemId + " нет");
        }
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        Item toDto = itemRepository.save(updateItem);
        return ItemMapper.mapToItemDto(toDto);
    }

    public void deleteItem(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Такого пользователя нет."));
        Booking booking = bookingRepository
                .findByItemIdAndBookerIdAndEndDateBefore(itemId, userId, LocalDateTime.now());
        if (booking == null) {
            throw new RequestException("Данный пользователь не арендовал этот предмет.");
        }
        Comment comment = commentRepository.save(CommentMapper.mapToComment(commentDto, itemId, userId));
        comment.setUser(user);
        return CommentMapper.mapToDto(comment);
    }
}
