package com.movietime.movie_service.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "shows")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Show {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String auditorium = "AUD-1";
    private Integer priceRegular;
    private Integer pricePremium;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    @PreUpdate void touch() { updatedAt = Instant.now(); }
}
