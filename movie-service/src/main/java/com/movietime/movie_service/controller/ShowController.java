package com.movietime.movie_service.controller;

import com.movietime.movie_service.dto.*;
import com.movietime.movie_service.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService service;
    public ShowController(ShowService service) { this.service = service; }

    @PostMapping
    public ShowDTO create(@Valid @RequestBody CreateShowRequest req) { return service.create(req); }

    @GetMapping("/{id}")
    public ShowDTO get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public List<ShowDTO> getShows(@RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return service.listByDate(date);
        }
        return service.listAll();  // when date is not provided
    }

    @GetMapping("/{id}/seats")
    public List<SeatDTO> seatLayout(@PathVariable Long id) { return service.seatLayoutForShow(id); }

    @GetMapping("/{id}/pricing")
    public Object pricing(@PathVariable Long id) {
        var s = service.get(id);
        return java.util.Map.of("priceRegular", s.priceRegular(), "pricePremium", s.pricePremium());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
