package com.kood.movieapi.controller;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    public MovieController(MovieService movieService) { this.movieService = movieService; }

    @GetMapping
    public Page<Movie> getAllMovies(
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long actor,
            Pageable pageable) {
        validatePageable(pageable);
        if (genre != null) return movieService.moviesByGenre(genre, pageable);
        if (year != null) return movieService.moviesByYear(year, pageable);
        if (actor != null) return movieService.moviesByActor(actor, pageable);
        return movieService.getAllMovies(pageable);
    }

    @GetMapping("/search")
    public Page<Movie> searchMovies(@RequestParam String title, Pageable pageable) {
        validatePageable(pageable);
        return movieService.searchMoviesByTitle(title, pageable);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) { return movieService.getMovieById(id); }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }

    @PatchMapping("/{id}")
    public Movie patchMovie(@PathVariable Long id, @RequestBody Map<String,Object> updates) {
        return movieService.patchMovie(id, updates);
    }

    @GetMapping("/{movieId}/actors")
    public List<Actor> actorsInMovie(@PathVariable Long movieId){
        return movieService.actorsInMovie(movieId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id,
                                            @RequestParam(name = "force", required = false) String force) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    private void validatePageable(Pageable pageable) {
        if (pageable == null) return;
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
}

