package com.kood.movieapi.service;

import com.kood.movieapi.entity.Actor;

import java.util.List;
import java.util.Map;

public interface ActorService {

    List<Actor> getAllActors();
    Actor getActorById(Long id);
    Actor createActor(Actor actor);
    Actor patchActor(Long id, Map<String, Object> updates);
    void deleteActor(Long id, boolean force);

    List<Actor> actorsByName(String name);
}
