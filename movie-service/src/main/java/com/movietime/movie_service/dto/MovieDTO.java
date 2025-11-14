package com.movietime.movie_service.dto;

import lombok.NoArgsConstructor;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    public Long id;
    public String title;
    public String description;
    public String language;
    public String duration;
    public String genre;
    public String rating;
    public String posterUrl;
    public LocalDate releaseDate;
    public boolean active;

}
