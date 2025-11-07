package com.movietime.movie_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @GetMapping("/protected")
    public ResponseEntity<Object> protectedList() {
        List<Map<String, Object>> movies = List.of(
                Map.of("id", 1, "title", "The Matrix", "year", 1999),
                Map.of("id", 2, "title", "Inception", "year", 2010)
        );

        return ResponseEntity.ok(Map.of("status", "ok", "movies", movies));
    }

    @GetMapping("/public")
    public ResponseEntity<Object> publicList() {
        List<String> titles = List.of("The Matrix", "Inception", "Interstellar");
        return ResponseEntity.ok(Map.of("status", "ok", "titles", titles));
    }
}
