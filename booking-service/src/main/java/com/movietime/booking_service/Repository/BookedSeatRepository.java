package com.movietime.booking_service.Repository;

import com.movietime.booking_service.Model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    List<BookedSeat> findByShowId(Long showId);
    boolean existsByShowIdAndSeatId(Long showId, Long seatId);
    void deleteByBookingId(Long bookingId);
}
