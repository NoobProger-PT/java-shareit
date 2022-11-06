package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDateDesc(long userId);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            long userId, LocalDateTime localDate1, LocalDateTime localDate2);

    List<Booking> findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(long userId,
                                                                        LocalDateTime localDate);

    List<Booking> findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(long userId, LocalDateTime localDate);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(long bookerId, BookingStatus status);

    List<Booking> findAllByItemIdInOrderByStartDateDesc(List<Long> ids);

    List<Booking> findAllByItemIdInAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            List<Long> id, LocalDateTime localDate1, LocalDateTime localDate2);

    List<Booking> findAllByItemIdInAndEndDateBeforeOrderByStartDateDesc(List<Long> id,
                                                                        LocalDateTime localDate);

    List<Booking> findAllByItemIdInAndStartDateAfterOrderByStartDateDesc(List<Long> id, LocalDateTime localDate);

    List<Booking> findAllByItemIdInAndStatusOrderByStartDateDesc(List<Long> id, BookingStatus status);

    Booking findByItemIdAndEndDateBeforeOrderByEndDateDesc(long itemId, LocalDateTime localDate);

    Booking findByItemIdAndStartDateAfterOrderByEndDateAsc(long itemId, LocalDateTime localDate);

    Booking findByItemIdAndBookerIdAndEndDateBefore(long itemId, long bookerId, LocalDateTime dateTime);
}
