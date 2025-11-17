package com.movietime.movie_service.controller;

import com.movietime.movie_service.Model.Movie;
import com.movietime.movie_service.Model.Show;
import com.movietime.movie_service.Repository.ShowRepository;
import com.movietime.movie_service.dto.*;
import com.movietime.movie_service.service.MovieService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService service;
    private final ShowRepository showRepository;
    public MovieController(MovieService service, ShowRepository showRepository) { 
        this.service = service; 
        this.showRepository = showRepository;
    }

    @GetMapping
    public List<MovieDTO> list(@RequestParam(required=false) Boolean activeOnly) {
        return service.getAll(activeOnly);
    }

    @GetMapping("/{id}")
    public MovieDTO get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public MovieDTO create(@Valid @RequestBody CreateMovieRequest req) {
        return service.create(req); }

    @PutMapping("/{id}")
    public MovieDTO update(@PathVariable Long id, @RequestBody CreateMovieRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping("/{id}/shows")
    public List<ShowDTO> showsForMovie(@PathVariable Long id) { return service.showsForMovie(id); }
    @GetMapping("/shows/{showId}")
    public ResponseEntity<ShowDetailsDTO> getShowDetails(@PathVariable Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        ShowDetailsDTO dto = toDTO(show);
        return ResponseEntity.ok(dto);
    }

    private ShowDetailsDTO toDTO(Show show) {

        ShowDetailsDTO dto = new ShowDetailsDTO();
        dto.setId(show.getId());
        dto.setStartTime(show.getStartTime());
        dto.setEndTime(show.getEndTime());
        dto.setAuditorium(show.getAuditorium());
        dto.setPriceRegular(show.getPriceRegular());
        dto.setPricePremium(show.getPricePremium());
        // Convert movie entity → MovieResponse DTO
        ShowDetailsDTO.MovieResponse movieDTO = new ShowDetailsDTO.MovieResponse();
        movieDTO.setId(show.getMovie().getId());
        movieDTO.setTitle(show.getMovie().getTitle());
        movieDTO.setGenre(show.getMovie().getGenre());
        movieDTO.setDuration(show.getMovie().getDuration());
        movieDTO.setPosterUrl(show.getMovie().getPosterUrl());
        dto.setMovie(movieDTO);
        return dto;
    }
    
}
