package com.kood.movieapi.service;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MovieService {

    List<Movie> getAllMovies();
    Page<Movie> getAllMovies(Pageable pageable);
    Movie getMovieById(Long id);
    Movie createMovie(Movie movie);
    Movie patchMovie(Long id, Map<String, Object> updates);
    void deleteMovie(Long id);

    List<Movie> moviesByGenre(Long genreId);
    Page<Movie> moviesByGenre(Long genreId, Pageable pageable);
    List<Movie> moviesByYear(Integer year);
    Page<Movie> moviesByYear(Integer year, Pageable pageable);
    List<Movie> moviesByActor(Long actorId);
    Page<Movie> moviesByActor(Long actorId, Pageable pageable);
    List<Actor> actorsInMovie(Long movieId);

    Page<Movie> searchMoviesByTitle(String title, Pageable pageable);
}
