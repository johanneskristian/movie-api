package com.kood.movieapi.repository;

import com.kood.movieapi.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByActors_Id(Long actorId);
    Page<Movie> findByActors_Id(Long actorId, Pageable pageable);

    List<Movie> findByReleaseYear(Integer releaseYear);
    Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable);

    List<Movie> findByGenres_Id(Long genreId);
    Page<Movie> findByGenres_Id(Long genreId, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
