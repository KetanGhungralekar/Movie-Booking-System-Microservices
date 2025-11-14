package com.movietime.movie_service.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateMovieRequest {
    @NotBlank public String title;
    @Size(max = 2000) public String description;
    public String language;
    public String duration;
    public String genre;
    public String rating;
    public String posterUrl;
    public LocalDate releaseDate;
    public Boolean active;
}
