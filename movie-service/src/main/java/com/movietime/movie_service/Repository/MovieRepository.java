package com.movietime.movie_service.Repository;

import com.movietime.movie_service.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
