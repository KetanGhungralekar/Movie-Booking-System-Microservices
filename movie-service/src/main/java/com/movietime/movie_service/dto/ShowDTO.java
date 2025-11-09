package com.movietime.movie_service.dto;

import java.time.LocalDateTime;

public class ShowDTO {
    public Long id;
    public Long movieId;
    public String movieTitle;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String auditorium;
    public Integer priceRegular;
    public Integer pricePremium;

    public ShowDTO(Long id, Long id1, String title, LocalDateTime startTime, LocalDateTime endTime, String auditorium, Integer priceRegular, Integer pricePremium) {
    }
}
