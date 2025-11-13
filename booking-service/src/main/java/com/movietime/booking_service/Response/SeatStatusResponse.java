package com.movietime.booking_service.Response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatusResponse {
    private List<Long> bookedSeatIds;
    private List<Long> lockedSeatIds;
}
