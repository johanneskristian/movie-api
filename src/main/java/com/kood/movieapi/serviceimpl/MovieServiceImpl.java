package com.kood.movieapi.serviceimpl;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.entity.Genre;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.repository.ActorRepository;
import com.kood.movieapi.repository.GenreRepository;
import com.kood.movieapi.repository.MovieRepository;
import com.kood.movieapi.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
                            ActorRepository actorRepository,
                            GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new com.kood.movieapi.exception.ResourceNotFoundException("Movie not found with id " + id));
    }

    @Override
    public Movie createMovie(Movie movie) {
        if (movie.getReleaseYear() != null && movie.getReleaseYear() > 0) {
            try {
                var legacyDate = java.time.LocalDate.of(movie.getReleaseYear(), 1, 1);
                movie.setReleaseDateLegacy(legacyDate);
            } catch (Exception ignored) {
            }
        }
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            List<Long> genreIds = movie.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            List<Genre> managedGenres = genreRepository.findAllById(genreIds);
            if (managedGenres.size() != genreIds.size()) {
                throw new IllegalArgumentException("One or more genres not found by provided IDs");
            }
            movie.setGenres(managedGenres);
        }

        if (movie.getActors() != null && !movie.getActors().isEmpty()) {
            List<Long> ids = movie.getActors().stream()
                    .map(Actor::getId)
                    .toList();
            List<Actor> managedActors = actorRepository.findAllById(ids);
            if (managedActors.size() != ids.size()) {
                throw new IllegalArgumentException("One or more actors not found by provided IDs");
            }
            movie.setActors(managedActors);
        }

        return movieRepository.save(movie);
    }


    @Override
    public Movie patchMovie(Long id, Map<String, Object> updates) {
        Movie movie = getMovieById(id);

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
                case "title" -> {
                    if (value instanceof String) {
                        movie.setTitle((String) value);
                    } else {
                        throw new IllegalArgumentException("Invalid type for title");
                    }
                }
                case "releaseYear" -> {
                    if (value instanceof Number) {
                        movie.setReleaseYear(((Number) value).intValue());
                    } else if (value instanceof String) {
                        movie.setReleaseYear(Integer.parseInt((String) value));
                    } else {
                        throw new IllegalArgumentException("Invalid type for releaseYear");
                    }
                }
                case "duration" -> {
                    if (value instanceof Number) {
                        movie.setDuration(((Number) value).intValue());
                    } else if (value instanceof String) {
                        movie.setDuration(Integer.parseInt((String) value));
                    } else {
                        throw new IllegalArgumentException("Invalid type for duration");
                    }
                }
                case "genres" -> {
                    if (value instanceof List) {
                        List<?> raw = (List<?>) value;
                        List<Long> ids = new ArrayList<>();
                        for (Object elem : raw) {
                            if (elem instanceof Genre g) {
                                if (g.getId() == null) {
                                    throw new IllegalArgumentException("Genre id is required in genres list");
                                }
                                ids.add(g.getId());
                            } else if (elem instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> gm = (Map<String, Object>) elem;
                                Object idObj = gm.get("id");
                                if (idObj == null) {
                                    throw new IllegalArgumentException("Genre id is required in genres list");
                                }
                                Long gid = idObj instanceof Number ? ((Number) idObj).longValue() : Long.parseLong(idObj.toString());
                                ids.add(gid);
                            } else {
                                throw new IllegalArgumentException("Invalid element type in genres list");
                            }
                        }
                        List<Genre> managedGenres = genreRepository.findAllById(ids);
                        if (managedGenres.size() != ids.size()) {
                            throw new IllegalArgumentException("One or more genres not found by provided IDs");
                        }
                        movie.setGenres(managedGenres);
                    } else {
                        throw new IllegalArgumentException("Invalid type for genres");
                    }
                }
                case "actors" -> {
                    if (value instanceof List) {
                        List<?> raw = (List<?>) value;
                        List<Long> ids = new ArrayList<>();
                        for (Object elem : raw) {
                            if (elem instanceof Actor a) {
                                if (a.getId() == null) {
                                    throw new IllegalArgumentException("Actor id is required in actors list");
                                }
                                ids.add(a.getId());
                            } else if (elem instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> am = (Map<String, Object>) elem;
                                Object idObj = am.get("id");
                                if (idObj == null) {
                                    throw new IllegalArgumentException("Actor id is required in actors list");
                                }
                                Long aid = idObj instanceof Number ? ((Number) idObj).longValue() : Long.parseLong(idObj.toString());
                                ids.add(aid);
                            } else {
                                throw new IllegalArgumentException("Invalid element type in actors list");
                            }
                        }
                        List<Actor> managedActors = actorRepository.findAllById(ids);
                        if (managedActors.size() != ids.size()) {
                            throw new IllegalArgumentException("One or more actors not found by provided IDs");
                        }
                        movie.setActors(managedActors);
                    } else {
                        throw new IllegalArgumentException("Invalid type for actors");
                    }
                }
                default -> throw new IllegalArgumentException("Unknown field: " + key);
            }
        });

        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public List<Movie> moviesByGenre(Long genreId) {
        return movieRepository.findByGenres_Id(genreId);
    }

    @Override
    public Page<Movie> moviesByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenres_Id(genreId, pageable);
    }

    @Override
    public List<Movie> moviesByYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
    }

    @Override
    public Page<Movie> moviesByYear(Integer year, Pageable pageable) {
        return movieRepository.findByReleaseYear(year, pageable);
    }

    @Override
    public List<Movie> moviesByActor(Long actorId) {
        return movieRepository.findByActors_Id(actorId);
    }

    @Override
    public Page<Movie> moviesByActor(Long actorId, Pageable pageable) {
        return movieRepository.findByActors_Id(actorId, pageable);
    }

    @Override
    public List<Actor> actorsInMovie(Long movieId) {
        Movie movie = getMovieById(movieId);
        return movie.getActors();
    }

    @Override
    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
