package com.kood.movieapi.service;

import com.kood.movieapi.entity.Genre;
import com.kood.movieapi.entity.Movie;

import java.util.List;
import java.util.Map;

public interface GenreService {

    List<Genre> getAllGenres();
    Genre getGenreById(Long id);
    Genre createGenre(Genre genre);
    Genre patchGenre(Long id, Map<String, Object> updates);
    void deleteGenre(Long id, boolean force);

    List<Movie> moviesInGenre(Long genreId);
}
