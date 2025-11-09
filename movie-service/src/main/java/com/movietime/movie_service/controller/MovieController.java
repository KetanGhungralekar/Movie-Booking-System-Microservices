package com.movietime.movie_service.controller;

import com.movietime.movie_service.Model.Movie;
import com.movietime.movie_service.dto.*;
import com.movietime.movie_service.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService service;
    public MovieController(MovieService service) { this.service = service; }

    @GetMapping
    public List<MovieDTO> list(@RequestParam(required=false) Boolean activeOnly) {
        return service.getAll(activeOnly);
    }

    @GetMapping("/{id}")
    public MovieDTO get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public Movie create(@Valid @RequestBody CreateMovieRequest req) {
        System.out.println("Creating movie: " + req.title);
        System.out.println("Description: " + req.description);
//        System.out.println();

        return service.create(req); }

    @PutMapping("/{id}")
    public MovieDTO update(@PathVariable Long id, @RequestBody CreateMovieRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping("/{id}/shows")
    public List<ShowDTO> showsForMovie(@PathVariable Long id) { return service.showsForMovie(id); }
}
