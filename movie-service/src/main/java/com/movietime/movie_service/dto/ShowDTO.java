package com.movietime.movie_service.dto;

import java.time.LocalDateTime;

public record ShowDTO(
        Long id,
        Long movieId,
        String movieTitle,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String auditorium,
        Integer priceRegular,
        Integer pricePremium
) {}
