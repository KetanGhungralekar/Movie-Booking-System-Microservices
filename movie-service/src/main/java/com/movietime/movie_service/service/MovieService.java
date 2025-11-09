package com.movietime.movie_service.service;

import com.movietime.movie_service.Model.Movie;
import com.movietime.movie_service.dto.*;
import java.util.List;

public interface MovieService {
    List<MovieDTO> getAll(Boolean onlyActive);
    MovieDTO get(Long id);
    Movie create(CreateMovieRequest req);
    MovieDTO update(Long id, CreateMovieRequest req);
    void delete(Long id);
    List<ShowDTO> showsForMovie(Long movieId);
}
