package com.kood.movieapi.serviceimpl;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.exception.ResourceNotFoundException;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.repository.ActorRepository;
import com.kood.movieapi.repository.MovieRepository;
import com.kood.movieapi.service.ActorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    public ActorServiceImpl(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    }

    @Override
    public Actor createActor(Actor actor) {
        if (actor.getName() == null || actor.getName().isEmpty()) {
            throw new IllegalArgumentException("Actor name cannot be null or empty");
        }
        if (actor.getBirthDate() == null) {
            throw new IllegalArgumentException("Actor birthDate cannot be null");
        }
        return actorRepository.save(actor);
    }

    @Override
    public Actor patchActor(Long id, Map<String, Object> updates) {
        Actor actor = getActorById(id);
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
                    if (value instanceof String) {
                        actor.setName((String) value);
                    } else {
                        throw new IllegalArgumentException("Invalid type for name");
                    }
                }
                case "birthDate" -> {
                    if (value instanceof String) {
                        actor.setBirthDate(LocalDate.parse((String) value));
                    } else if (value instanceof LocalDate) {
                        actor.setBirthDate((LocalDate) value);
                    } else {
                        throw new IllegalArgumentException("Invalid type for birthDate");
                    }
                }
                default -> {
                    System.out.println("Warning: Unknown field '" + key + "' ignored");
                }
            }
        });
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(Long id, boolean force) {
        Actor actor = getActorById(id);
        List<Movie> movies = movieRepository.findByActors_Id(id);
        int count = movies.size();
        if (count > 0 && !force) {
            throw new IllegalArgumentException("Unable to delete actor '" + actor.getName() + "' as they are associated with " + count + " movies");
        }

        if (count > 0) {
            for (Movie m : movies) {
                if (m.getActors() != null) {
                    m.getActors().removeIf(a -> a.getId().equals(id));
                }
            }
            movieRepository.saveAll(movies);
        }
        actorRepository.delete(actor);
    }

    @Override
    public List<Actor> actorsByName(String name) {
        return actorRepository.findByNameContainingIgnoreCase(name);
    }
}
