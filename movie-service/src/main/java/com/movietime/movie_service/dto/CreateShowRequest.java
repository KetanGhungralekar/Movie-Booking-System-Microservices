package com.movietime.movie_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateShowRequest(
        Long movieId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String auditorium,
        Integer priceRegular,
        Integer pricePremium
) {}
