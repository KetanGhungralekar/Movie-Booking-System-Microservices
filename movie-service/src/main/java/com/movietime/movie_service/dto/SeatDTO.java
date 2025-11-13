package com.movietime.movie_service.dto;

import com.movietime.movie_service.Model.SeatType;

public class SeatDTO {
    public Long id;
    public String rowLabel;
    public Integer seatNumber;
    public SeatType type;
    public String auditorium;

    public SeatDTO(Long id, String rowLabel, Integer seatNumber, SeatType type, String auditorium) {
    }
}
