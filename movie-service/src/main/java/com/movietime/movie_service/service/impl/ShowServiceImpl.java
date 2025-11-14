package com.movietime.movie_service.service.impl;

import com.movietime.movie_service.dto.*;
import com.movietime.movie_service.Model.*;
import com.movietime.movie_service.Repository.*;
import com.movietime.movie_service.exception.NotFoundException;
import com.movietime.movie_service.service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service @Transactional
public class ShowServiceImpl implements ShowService {
    private final ShowRepository shows;
    private final MovieRepository movies;
    private final TheatreSeatRepository seats;

    public ShowServiceImpl(ShowRepository shows, MovieRepository movies, TheatreSeatRepository seats) {
        this.shows = shows; this.movies = movies; this.seats = seats;
    }

    @Override
    public ShowDTO create(CreateShowRequest r) {
        Movie movie = movies.findById(r.movieId())
                .orElseThrow(() -> new NotFoundException("Movie not found: " + r.movieId()));
        Show s = Show.builder()
                .movie(movie)
                .startTime(r.startTime())
                .endTime(r.endTime())
                .auditorium(r.auditorium() == null ? "AUD-1" : r.auditorium())
                .priceRegular(r.priceRegular())
                .pricePremium(r.pricePremium())
                .build();
        return toDto(shows.save(s));
    }

    @Override
    public ShowDTO get(Long id) {
        return shows.findById(id).map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Show not found: " + id));
    }

    @Override
    public List<ShowDTO> listByDate(LocalDate date) {
        var start = date.atStartOfDay();
        var end = start.plusDays(1);
        return shows.findByStartTimeBetween(start, end).stream().map(this::toDto).toList();
    }

    @Override
    public List<SeatDTO> seatLayoutForShow(Long showId) {
        shows.findById(showId).orElseThrow(() -> new NotFoundException("Show not found: " + showId));
        return seats.findByAuditoriumOrderByRowLabelAscSeatNumberAsc("AUD-1")
                .stream().map(ts -> new SeatDTO(ts.getId(), ts.getRowLabel(),
                        ts.getSeatNumber(), ts.getType(), ts.getAuditorium()))
                .toList();
    }

    private ShowDTO toDto(Show s) {
        return new ShowDTO(s.getId(), s.getMovie().getId(), s.getMovie().getTitle(),
                s.getStartTime(), s.getEndTime(), s.getAuditorium(),
                s.getPriceRegular(), s.getPricePremium());
    }

    @Override
    public List<ShowDTO> listAll() {
        return shows.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!shows.existsById(id)) {
            throw new NotFoundException("Show not found: " + id);
        }
        shows.deleteById(id);
    }


}
