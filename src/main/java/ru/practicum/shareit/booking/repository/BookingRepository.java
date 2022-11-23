package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long userId, Sort sort);

    Page<Booking> findAllByBookerId(long userId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfter(
            long userId, LocalDateTime localDate1, LocalDateTime localDate2, Sort sort);

    Page<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfter(
            long userId, LocalDateTime localDate1, LocalDateTime localDate2, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndDateBefore(long userId, LocalDateTime localDate, Sort sort);

    Page<Booking> findAllByBookerIdAndEndDateBefore(long userId, LocalDateTime localDate, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartDateAfter(long userId, LocalDateTime localDate, Sort sort);

    Page<Booking> findAllByBookerIdAndStartDateAfter(long userId, LocalDateTime localDate, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status, Sort sort);

    Page<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemIdIn(List<Long> ids, Sort sort);

    Page<Booking> findAllByItemIdIn(List<Long> ids, Pageable pageable);

    List<Booking> findAllByItemIdInAndStartDateBeforeAndEndDateAfter(
            List<Long> id, LocalDateTime localDate1, LocalDateTime localDate2, Sort sort);

    Page<Booking> findAllByItemIdInAndStartDateBeforeAndEndDateAfter(
            List<Long> id, LocalDateTime localDate1, LocalDateTime localDate2, Pageable pageable);

    List<Booking> findAllByItemIdInAndEndDateBefore(List<Long> id, LocalDateTime localDate, Sort sort);

    Page<Booking> findAllByItemIdInAndEndDateBefore(List<Long> id, LocalDateTime localDate, Pageable pageable);

    List<Booking> findAllByItemIdInAndEndDateBeforeAndStatus(List<Long> id, LocalDateTime localDate,
                                                             BookingStatus status, Sort sort);

    List<Booking> findAllByItemIdInAndStartDateAfterAndStatus(List<Long> id, LocalDateTime localDate,
                                                             BookingStatus status, Sort sort);

    List<Booking> findAllByItemIdInAndStartDateAfter(List<Long> id, LocalDateTime localDate, Sort sort);

    Page<Booking> findAllByItemIdInAndStartDateAfter(List<Long> id, LocalDateTime localDate, Pageable pageable);

    List<Booking> findAllByItemIdInAndStatus(List<Long> id, BookingStatus status, Sort sort);

    Page<Booking> findAllByItemIdInAndStatus(List<Long> id, BookingStatus status, Pageable pageable);

    Booking findByItemIdAndEndDateBefore(long itemId, LocalDateTime localDate, Sort sort);

    Booking findByItemIdAndStartDateAfter(long itemId, LocalDateTime localDate, Sort sort);

    Optional<Booking> findByItemIdAndBookerIdAndEndDateBefore(long itemId, long bookerId, LocalDateTime dateTime);
}
