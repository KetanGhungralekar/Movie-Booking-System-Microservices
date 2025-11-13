package com.movietime.booking_service.Model;
import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booked_seats",
       uniqueConstraints = @UniqueConstraint(columnNames = {"show_id","seat_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookedSeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showId;
    private Long seatId;
    private Long bookingId;
    private Instant bookedAt = Instant.now();
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (this.bookedAt == null) this.bookedAt = now;
    }
}

