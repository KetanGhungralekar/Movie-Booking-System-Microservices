package com.movietime.notification_service.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShowDetailsDTO {
    private Long id;
    private MovieDTO movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String auditorium;
    private Integer priceRegular;
    private Integer pricePremium;

    @Data
    public static class MovieDTO {
        private Long id;
        private String title;
        private String genre;
        private String duration;
        private String posterUrl;
    }
}
