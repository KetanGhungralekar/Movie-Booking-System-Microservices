package com.movietime.movie_service.dto;

import java.time.LocalDate;

public class MovieDTO {
    public Long id;
    public String title;
    public String description;
    public String language;
    public Integer duration;
    public String genre;
    public String posterUrl;
    public LocalDate releaseDate;
    public boolean active;

    public MovieDTO(Long id, String title, String description, String language, Integer duration, String genre, String posterUrl, LocalDate releaseDate, boolean active) {
    }
}
