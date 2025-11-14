package com.movietime.movie_service.service;

import com.movietime.movie_service.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface ShowService {
    ShowDTO create(CreateShowRequest req);
    ShowDTO get(Long id);
    List<ShowDTO> listByDate(LocalDate date);
    List<SeatDTO> seatLayoutForShow(Long showId);
    List<ShowDTO> listAll();
    void delete(Long id);

}
