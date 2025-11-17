package com.movietime.booking_service.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.movietime.booking_service.Config.RabbitMQConfig;
import com.movietime.booking_service.DTO.CreateBookingRequest;
import com.movietime.booking_service.DTO.SeatSelection;
import com.movietime.booking_service.Event.PaymentSuccessEvent;
import com.movietime.booking_service.Model.BookedSeat;
import com.movietime.booking_service.Model.Booking;
import com.movietime.booking_service.Model.BookingSeat;
import com.movietime.booking_service.Model.BookingStatus;
import com.movietime.booking_service.Repository.BookedSeatRepository;
import com.movietime.booking_service.Repository.BookingRepository;
import com.movietime.booking_service.Repository.BookingSeatRepository;
import com.movietime.booking_service.Response.BookingResponse;
import com.movietime.booking_service.Response.BookingResponseDTO;
import com.movietime.booking_service.Response.SeatStatusResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final StringRedisTemplate redisTemplate;
    private final BookedSeatRepository bookedSeatRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final long LOCK_TTL = 600; // seconds

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest req, String userId) {
        Long showId = req.getShowId();

        // 1️⃣ Try locking each seat in Redis
        for (SeatSelection seat : req.getSeats()) {
            String key = "lock:show:" + showId + ":seat:" + seat.getSeatId();
            String value = seat.getRowLabel() + "-" + seat.getSeatNumber() + "-" + seat.getSeatType();

            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(key, value, LOCK_TTL, TimeUnit.SECONDS);

            if (Boolean.FALSE.equals(success)) {
                throw new IllegalStateException("Seat already locked or booked: " + seat.getSeatId());
            }
        }

        // 2️⃣ Create Booking record
        Booking booking = Booking.builder()
                .showId(showId)
                .userId(userId) // Long type now
                .status(BookingStatus.PENDING_PAYMENT)
                .totalAmount(req.getTotalAmount())
                .currency("INR")
                .lockId(UUID.randomUUID().toString())
                .build();
        bookingRepository.save(booking);

        // 3️⃣ Save seat details in booking_seats table
        for (SeatSelection seat : req.getSeats()) {
            BookingSeat seatEntity = BookingSeat.builder()
                    .booking(booking)
                    .seatId(seat.getSeatId())
                    .rowLabel(seat.getRowLabel())
                    .seatNumber(seat.getSeatNumber())
                    .seatType(seat.getSeatType())
                    .price(seat.getPrice())
                    .build();

            bookingSeatRepository.save(seatEntity);
        }

        return BookingResponse.builder()
            .id(booking.getId())
            .showId(booking.getShowId())
            .userId(booking.getUserId())
            .status(booking.getStatus())
            .totalAmount(booking.getTotalAmount())
            .currency(booking.getCurrency())
            .paymentId(booking.getPaymentId())
            .lockId(booking.getLockId())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }
    @Transactional
    public BookingResponse confirmPayment(Long bookingId, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Ensure booking hasn’t already been processed
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking already processed");
        }

        // Update booking status and payment reference
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        bookingRepository.save(booking);

        // Fetch seats for this booking
        List<BookingSeat> seats = bookingSeatRepository.findByBookingId(booking.getId());

        for (BookingSeat seat : seats) {
            // Save into booked_seats table (ensures seat uniqueness per show)
            try {
                bookedSeatRepository.save(
                    BookedSeat.builder()
                            .showId(booking.getShowId())
                            .seatId(seat.getSeatId())
                            .bookingId(booking.getId())
                            .bookedAt(Instant.now())
                            .build()
                );
            } catch (DataIntegrityViolationException e) {
                throw new IllegalStateException("Seat already booked by another user");
            }

            // Remove Redis lock if present
            String key = "lock:show:" + booking.getShowId() + ":seat:" + seat.getSeatId();
            redisTemplate.delete(key);
        }
        publishBookingConfirmed(booking);
        return BookingResponse.builder()
            .id(booking.getId())
            .showId(booking.getShowId())
            .userId(booking.getUserId())
            .status(booking.getStatus())
            .totalAmount(booking.getTotalAmount())
            .currency(booking.getCurrency())
            .paymentId(booking.getPaymentId())
            .lockId(booking.getLockId())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }

    public List<Long> getBookedSeats(Long showId) {
        return bookedSeatRepository.findByShowId(showId)
                .stream()
                .map(BookedSeat::getSeatId)
                .toList();
    }
    public SeatStatusResponse getSeatStatus(Long showId) {
        // Get booked seats from DB
        List<Long> bookedSeatIds = bookedSeatRepository
            .findByShowId(showId)
            .stream()
            .map(BookedSeat::getSeatId)
            .toList();

        // Get locked seats from Redis
        Set<String> lockedKeys = redisTemplate.keys("lock:show:" + showId + ":seat:*");
        List<Long> lockedSeatIds = new ArrayList<>();
        if (lockedKeys != null) {
            for (String key : lockedKeys) {
                String[] parts = key.split(":seat:");
                lockedSeatIds.add(Long.valueOf(parts[1]));
            }
        }

        return new SeatStatusResponse(bookedSeatIds, lockedSeatIds);
    }
    public List<BookingResponseDTO> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId).stream().map(booking -> {
            BookingResponseDTO dto = new BookingResponseDTO();
            dto.setId(booking.getId());
            dto.setUserId(booking.getUserId());
            dto.setShowId(booking.getShowId());
            dto.setStatus(booking.getStatus().toString());
            dto.setTotalAmount(booking.getTotalAmount());
            dto.setPaymentId(booking.getPaymentId());

            dto.setSeats(
                booking.getSeats().stream().map(seat -> {
                    BookingResponseDTO.SeatDTO s = new BookingResponseDTO.SeatDTO();
                    s.setSeatNumber(seat.getSeatNumber());
                    s.setPrice(seat.getPrice());
                    return s;
                }).collect(Collectors.toList())
            );

            return dto;
        }).collect(Collectors.toList());
    }
    @Scheduled(fixedRate = 60000) // runs every minute
    @Transactional
    public void cancelExpiredBookings() {
        Instant now = Instant.now();
        List<Booking> pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING_PAYMENT);

        for (Booking booking : pendingBookings) {
            // check if Redis key still exists for any of its seats
            boolean anyLocked = booking.getSeats().stream().anyMatch(seat -> {
                String key = "lock:show:" + booking.getShowId() + ":seat:" + seat.getSeatId();
                return Boolean.TRUE.equals(redisTemplate.hasKey(key));
            });

            if (!anyLocked) {
                // All locks expired → cancel booking
                booking.setStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);
            }
        }
    }
    
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        boolean wasConfirmed = booking.getStatus() == BookingStatus.CONFIRMED;

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        for (BookingSeat seat : booking.getSeats()) {
            String key = "lock:show:" + booking.getShowId() + ":seat:" + seat.getSeatId();
            redisTemplate.delete(key);
        }

        if (wasConfirmed) {
            bookedSeatRepository.deleteByBookingId(bookingId);
        }

        return BookingResponse.builder()
            .id(booking.getId())
            .showId(booking.getShowId())
            .userId(booking.getUserId())
            .status(booking.getStatus())
            .totalAmount(booking.getTotalAmount())
            .currency(booking.getCurrency())
            .paymentId(booking.getPaymentId())
            .lockId(booking.getLockId())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }


    public void publishBookingConfirmed(Booking booking) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("bookingId", booking.getId());
        payload.put("userEmail", booking.getUserId());
        payload.put("status", booking.getStatus().name());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BOOKING_EXCHANGE,
                RabbitMQConfig.BOOKING_ROUTING_KEY,
                payload
        );

        System.out.println("Published booking confirmation event for: " + booking.getId());
    }
    @RabbitListener(queues = "payment.success.queue")
    public void handlePaymentEvent(PaymentSuccessEvent event) {
        System.out.println("Received Payment Success Event for Booking ID: " + event.getPaymentId());
        confirmPayment(event.getBookingId(), event.getPaymentId());
    }
}

