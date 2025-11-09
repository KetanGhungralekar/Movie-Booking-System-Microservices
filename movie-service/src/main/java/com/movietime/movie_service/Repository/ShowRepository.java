package com.movietime.movie_service.Repository;

import com.movietime.movie_service.Model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovie_Id(Long movieId);
    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
