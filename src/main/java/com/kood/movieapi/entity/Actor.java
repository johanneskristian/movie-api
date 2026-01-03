package com.kood.movieapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "movies")
@Entity
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "Actor name is required")
    private String name;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "actors")
    @JsonIgnore
    private List<Movie> movies;
}
