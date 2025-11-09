package com.movietime.movie_service.Repository;

import com.movietime.movie_service.Model.TheatreSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TheatreSeatRepository extends JpaRepository<TheatreSeat, Long> {
    List<TheatreSeat> findByAuditoriumOrderByRowLabelAscSeatNumberAsc(String auditorium);
}
