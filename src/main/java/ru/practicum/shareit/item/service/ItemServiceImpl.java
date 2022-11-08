package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
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

    public List<ItemWithBookingDto> getAll(long userId) {
        List<ItemWithBookingDto> result;
        List<ItemDto> items = itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        List<Long> itemId = items.stream().map(i -> i.getId()).collect(Collectors.toList());
        List<Booking> last = bookingRepository.findAllByItemIdInAndEndDateBefore(itemId, LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "item_id"));
        List<Booking> next = bookingRepository.findAllByItemIdInAndStartDateAfter(itemId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "item_id"));

        result = items.stream()
                .map(i -> ItemMapper.mapToItemWithBookingDto(i))
                .map(i -> {
                    for (Booking b : last) {
                        if (i.getId() == b.getItem().getId()) {
                            i.setLastBooking(BookingMapper.mapToShortDto(b));
                        }
                        break;
                    }
                    for (Booking b : next) {
                        if (i.getId() == b.getItem().getId()) {
                            i.setNextBooking(BookingMapper.mapToShortDto(b));
                        }
                        break;
                    }
                    return i;
                }).collect(Collectors.toList());
        return result;
    }

    @Transactional
    public ItemDto addNew(long userId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Пользователя с id " + userId + " не существует."));
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, userId));
        return ItemMapper.mapToItemDto(item);
    }

    public ItemWithBookingAndCommentDto findById(long itemId, long userId) {
        ItemWithBookingDto itemWithBookingDto;
        ItemDto itemDto = ItemMapper.mapToItemDto(itemRepository.findById(itemId).get());
        BookingDtoShort lastBook = BookingMapper.mapToShortDto(
                bookingRepository.findByItemIdAndEndDateBefore(itemId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "endDate")));
        BookingDtoShort nextBook = BookingMapper.mapToShortDto(
                bookingRepository.findByItemIdAndStartDateAfter(itemId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.ASC, "endDate")));
        if (itemDto.getOwner().getId() == userId) {
            itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto, lastBook, nextBook);
        } else {
            itemWithBookingDto = ItemMapper.mapToItemWithBookingDto(itemDto);
        }
        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
        return ItemMapper.mapToItemBookingCommentDto(itemWithBookingDto, comments);
    }

    public List<ItemDto> findByText(String text) {
        return itemRepository.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);
        itemDto.setOwner(new UserDto(userId));
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemDontExistsException("Вещи с таким id нет"));
        if (updateItem.getOwner().getId() != userId) {
            throw new UserDontExistsException("У пользователя с id " + userId + " вещи с id " + itemId + " нет");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.mapToItemDto(updateItem);
    }

    public void delete(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserDontExistsException("Такого пользователя нет."));
        Booking booking = bookingRepository.findByItemIdAndBookerIdAndEndDateBefore(
                itemId, userId, LocalDateTime.now()).orElseThrow(() ->
                new RequestException("Данный пользователь не арендовал этот предмет."));
        if (booking == null) {
            throw new RequestException("Данный пользователь не арендовал этот предмет.");
        }
        Comment comment = commentRepository.save(CommentMapper.mapToComment(commentDto, itemId, userId));
        comment.setUser(user);
        return CommentMapper.mapToDto(comment);
    }
}
