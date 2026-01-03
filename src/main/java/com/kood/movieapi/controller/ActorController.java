package com.kood.movieapi.controller;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;
    public ActorController(ActorService actorService) { this.actorService = actorService; }

    @GetMapping
    public List<Actor> getAllActors(@RequestParam(required = false) String name) {
        if (name != null) return actorService.actorsByName(name);
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) { return actorService.getActorById(id); }

    @PostMapping
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(actor));
    }

    @PatchMapping("/{id}")
    public Actor patchActor(@PathVariable Long id, @RequestBody Map<String,Object> updates) {
        return actorService.patchActor(id, updates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id,
                                            @RequestParam(name = "force", required = false) String force) {
        boolean forceBool = parseBooleanLenient(force);
        actorService.deleteActor(id, forceBool);
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
