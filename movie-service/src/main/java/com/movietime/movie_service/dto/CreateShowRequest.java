package com.movietime.movie_service.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CreateShowRequest {
    @NotNull public Long movieId;
    @NotNull public LocalDateTime startTime;
    @NotNull public LocalDateTime endTime;
    public String auditorium;
    @NotNull @Positive public Integer priceRegular;
    @NotNull @Positive public Integer pricePremium;
}
