package com.kood.movieapi.controller;

import com.kood.movieapi.entity.Genre;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/genres"})
public class GenreController {

    private final GenreService genreService;
    public GenreController(GenreService genreService) { this.genreService = genreService; }

    @GetMapping
    public List<Genre> getAllGenres() { return genreService.getAllGenres(); }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) { return genreService.getGenreById(id); }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(genre));
    }

    @PatchMapping("/{id}")
    public Genre patchGenre(@PathVariable Long id, @RequestBody Map<String,Object> updates) {
        return genreService.patchGenre(id, updates);
    }

    @GetMapping("/{id}/movies")
    public List<Movie> getMoviesInGenre(@PathVariable Long id) {
        return genreService.moviesInGenre(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id,
                                            @RequestParam(name = "force", required = false) String force) {
        boolean forceBool = parseBooleanLenient(force);
        genreService.deleteGenre(id, forceBool);
        return ResponseEntity.noContent().build();
    }

    private boolean parseBooleanLenient(String value) {
        if (value == null) return false;
        String v = value.trim().toLowerCase();
        if (v.isEmpty()) return true;

        String first = v.split("[|,;]", 2)[0].trim();
        return first.equals("true") || first.equals("1") || first.equals("yes") || first.equals("y");
    }
}
