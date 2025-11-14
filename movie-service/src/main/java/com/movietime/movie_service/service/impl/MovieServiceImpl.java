package com.movietime.movie_service.service.impl;

import com.movietime.movie_service.dto.*;
import com.movietime.movie_service.Model.*;
import com.movietime.movie_service.Repository.*;
import com.movietime.movie_service.exception.NotFoundException;
import com.movietime.movie_service.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movies;
    private final ShowRepository shows;

    public MovieServiceImpl(MovieRepository movies, ShowRepository shows) {
        this.movies = movies; this.shows = shows;
    }

    @Override
    public List<MovieDTO> getAll(Boolean onlyActive) {
        return movies.findAll().stream()
                .filter(m -> onlyActive == null || !onlyActive || m.isActive())
                .map(this::toDto).toList();
    }

    @Override
    public MovieDTO get(Long id) {
        return movies.findById(id).map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Movie not found: " + id));
    }

    @Override
    public MovieDTO create(CreateMovieRequest r) {
        Movie m = Movie.builder()
                .title(r.title).description(r.description)
                .language(r.language).duration(r.duration)
                .genre(r.genre).rating(r.rating).posterUrl(r.posterUrl)
                .releaseDate(r.releaseDate)
                .active(r.active == null ? true : r.active)
                .build();
        System.out.println("Saving movie: " + m.getTitle());
        // persist and return the saved entity so id and timestamps are generated
        return toDto(movies.save(m));
    }

    @Override
    public MovieDTO update(Long id, CreateMovieRequest r) {
        Movie m = movies.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found: " + id));
        if (r.title != null) m.setTitle(r.title);
        if (r.description != null) m.setDescription(r.description);
        if (r.language != null) m.setLanguage(r.language);
        if (r.duration != null) m.setDuration(r.duration);
        if (r.genre != null) m.setGenre(r.genre);
        if (r.rating != null) m.setRating(r.rating);
        if (r.posterUrl != null) m.setPosterUrl(r.posterUrl);
        if (r.releaseDate != null) m.setReleaseDate(r.releaseDate);
        if (r.active != null) m.setActive(r.active);
        return toDto(m);
    }

    @Override
    public void delete(Long id) { movies.deleteById(id); }

    @Override
    public List<ShowDTO> showsForMovie(Long movieId) {
        return shows.findByMovie_Id(movieId).stream().map(this::toDto).toList();
    }

    private MovieDTO toDto(Movie m) {
        return new MovieDTO(m.getId(), m.getTitle(), m.getDescription(),
                m.getLanguage(), m.getDuration(), m.getGenre(), m.getRating(),
                m.getPosterUrl(), m.getReleaseDate(), m.isActive());
    }

    private ShowDTO toDto(Show s) {
        return new ShowDTO(s.getId(), s.getMovie().getId(), s.getMovie().getTitle(),
                s.getStartTime(), s.getEndTime(), s.getAuditorium(),
                s.getPriceRegular(), s.getPricePremium());
    }
}
