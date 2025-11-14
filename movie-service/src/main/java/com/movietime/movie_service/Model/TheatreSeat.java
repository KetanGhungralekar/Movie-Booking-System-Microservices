package com.movietime.movie_service.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theatre_seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"row_label", "seat_number"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheatreSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_label")
    private String rowLabel;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Builder.Default
    private String auditorium = "AUD-1";
}
