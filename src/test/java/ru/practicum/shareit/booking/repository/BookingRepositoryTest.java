package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    Item item = new Item();
    ItemRequest itemRequest = new ItemRequest();
    User user = new User();
    Booking booking = new Booking();

    @BeforeEach
    public void createData() {
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@mail.ru");
        userRepository.save(user);

        itemRequest.setRequestor(user);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemRepository.save(item);

        booking.setId(1L);
        booking.setStartDate(LocalDateTime.of(2023,10,10,10,10));
        booking.setEndDate(LocalDateTime.of(2024,10,10,10,10));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    @Test
    void findAllByBookerId() {
        List<Booking> bookings = bookingRepository.findAllByBookerId(user.getId(),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByBookerIdAndStartDateBeforeAndEndDateAfter() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(user.getId(),
                LocalDateTime.of(2023,12,10,10,10),
                LocalDateTime.of(2023,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByBookerIdAndEndDateBefore() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndDateBefore(user.getId(),
                LocalDateTime.of(2025,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByBookerIdAndStartDateAfter() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStartDateAfter(user.getId(),
                LocalDateTime.of(2022,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByBookerIdAndStatus() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(user.getId(),
                BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdIn() {
        List<Booking> bookings = bookingRepository.findAllByItemIdIn(List.of(1L),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndStartDateBeforeAndEndDateAfter() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStartDateBeforeAndEndDateAfter(List.of(1L),
                LocalDateTime.of(2023,11,10,10,10),
                LocalDateTime.of(2023,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndEndDateBefore() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndEndDateBefore(List.of(1L),
                LocalDateTime.of(2026,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndEndDateBeforeAndStatus() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndEndDateBeforeAndStatus(List.of(1L),
                LocalDateTime.of(2026,12,10,10,10),
                BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndStartDateAfterAndStatus() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStartDateAfterAndStatus(List.of(1L),
                LocalDateTime.of(2020,12,10,10,10),
                BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndStartDateAfter() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStartDateAfter(List.of(1L),
                LocalDateTime.of(2020,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findAllByItemIdInAndStatus() {
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatus(List.of(1L),
                BookingStatus.APPROVED,
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findByItemIdAndEndDateBefore() {
        Booking bookings = bookingRepository.findByItemIdAndEndDateBefore(1L,
                LocalDateTime.of(2026,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findByItemIdAndStartDateAfter() {
        Booking bookings = bookingRepository.findByItemIdAndStartDateAfter(1L,
                LocalDateTime.of(2020,12,10,10,10),
                Sort.by(Sort.Direction.DESC, "startDate"));
        assertThat(bookings.getBooker().getName(), equalTo(user.getName()));
    }

    @Test
    void findByItemIdAndBookerIdAndEndDateBefore() {
        Booking bookings = bookingRepository.findByItemIdAndBookerIdAndEndDateBefore(1L, 1L,
                LocalDateTime.of(2026,12,10,10,10)).get();
        assertThat(bookings.getBooker().getName(), equalTo(user.getName()));
    }
}