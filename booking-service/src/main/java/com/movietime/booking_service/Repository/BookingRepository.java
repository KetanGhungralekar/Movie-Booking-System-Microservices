package com.movietime.booking_service.Repository;
import com.movietime.booking_service.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByStatus(BookingStatus status);
}
