package com.kood.movieapi.serviceimpl;

import com.kood.movieapi.entity.Genre;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.exception.ResourceNotFoundException;
import com.kood.movieapi.repository.GenreRepository;
import com.kood.movieapi.repository.MovieRepository;
import com.kood.movieapi.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public GenreServiceImpl(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
    }

    @Override
    public Genre createGenre(Genre genre) {
        if (genre.getName() == null || genre.getName().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }
        return genreRepository.save(genre);
    }

    @Override
    public Genre patchGenre(Long id, Map<String, Object> updates) {
        Genre genre = getGenreById(id);
        updates.forEach((key, value) -> {
            switch (key) {
                case "id" -> {
                    if (value != null) {
                        Long bodyId;
                        if (value instanceof Number n) {
                            bodyId = n.longValue();
                        } else {
                            try {
                                bodyId = Long.parseLong(value.toString());
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("Invalid type for id; must be a number");
                            }
                        }
                        if (!id.equals(bodyId)) {
                            throw new IllegalArgumentException("Path id " + id + " does not match body id " + bodyId);
                        }
                    }
                }
                case "name" -> {
                    if (value instanceof String name) {
                        if (name.isEmpty()) {
                            throw new IllegalArgumentException("Genre name cannot be null or empty");
                        }
                        genre.setName(name);
                    } else {
                        throw new IllegalArgumentException("Invalid type for name");
                    }
                }
                default -> {
                    System.out.println("Warning: Unknown field '" + key + "' ignored");
                }
            }
        });
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id, boolean force) {
        Genre genre = getGenreById(id);
        List<Movie> movies = movieRepository.findByGenres_Id(id);
        int count = movies.size();
        if (count > 0 && !force) {
            throw new IllegalArgumentException("Cannot delete genre '" + genre.getName() + "' because it has " + count + " associated movies");
        }

        if (count > 0) {
            for (Movie m : movies) {
                if (m.getGenres() != null) {
                    m.getGenres().removeIf(g -> g.getId().equals(id));
                }
            }
            movieRepository.saveAll(movies);
        }
        genreRepository.delete(genre);
    }

    @Override
    public List<Movie> moviesInGenre(Long genreId) {
        getGenreById(genreId);
        return movieRepository.findByGenres_Id(genreId);
    }
}
