package com.movietime.movie_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShowDetailsDTO {
    private Long id;
    private MovieResponse movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String auditorium;
    private Integer priceRegular;
    private Integer pricePremium;

    @Data
    public static class MovieResponse {
        private Long id;
        private String title;
        private String genre;
        private String duration;
        private String posterUrl;
    }
}
