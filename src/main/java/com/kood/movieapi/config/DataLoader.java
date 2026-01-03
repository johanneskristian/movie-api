package com.kood.movieapi.config;

import com.kood.movieapi.entity.Actor;
import com.kood.movieapi.entity.Genre;
import com.kood.movieapi.entity.Movie;
import com.kood.movieapi.repository.ActorRepository;
import com.kood.movieapi.repository.GenreRepository;
import com.kood.movieapi.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public DataLoader(ActorRepository actorRepository, GenreRepository genreRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        movieRepository.deleteAll();
        actorRepository.deleteAll();
        genreRepository.deleteAll();

        Actor tomHanks = new Actor(null, "Tom Hanks", LocalDate.of(1956,7,9), null);
        Actor merylStreep = new Actor(null, "Meryl Streep", LocalDate.of(1949,6,22), null);
        Actor leonardo = new Actor(null, "Leonardo DiCaprio", LocalDate.of(1974,11,11), null);
        Actor scarlett = new Actor(null, "Scarlett Johansson", LocalDate.of(1984,11,22), null);
        Actor denzel = new Actor(null, "Denzel Washington", LocalDate.of(1954,12,28), null);
        Actor natalie = new Actor(null, "Natalie Portman", LocalDate.of(1981,6,9), null);
        Actor keanu = new Actor(null, "Keanu Reeves", LocalDate.of(1964,9,2), null);
        Actor emma = new Actor(null, "Emma Stone", LocalDate.of(1988,11,6), null);
        Actor ryan = new Actor(null, "Ryan Gosling", LocalDate.of(1980,11,12), null);
        Actor robert = new Actor(null, "Robert Downey Jr.", LocalDate.of(1965,4,4), null);
        Actor chris = new Actor(null, "Chris Evans", LocalDate.of(1981,6,13), null);
        Actor zendaya = new Actor(null, "Zendaya", LocalDate.of(1996,9,1), null);
        Actor tomHolland = new Actor(null, "Tom Holland", LocalDate.of(1996,6,1), null);
        Actor brie = new Actor(null, "Brie Larson", LocalDate.of(1989,10,1), null);
        Actor florence = new Actor(null, "Florence Pugh", LocalDate.of(1996,1,3), null);

        actorRepository.saveAll(Arrays.asList(
                tomHanks, merylStreep, leonardo, scarlett, denzel,
                natalie, keanu, emma, ryan, robert, chris, zendaya,
                tomHolland, brie, florence
        ));

        Genre action = new Genre(null, "Action", null);
        Genre drama = new Genre(null, "Drama", null);
        Genre comedy = new Genre(null, "Comedy", null);
        Genre sciFi = new Genre(null, "Sci-Fi", null);
        Genre thriller = new Genre(null, "Thriller", null);
        Genre romance = new Genre(null, "Romance", null);

        genreRepository.saveAll(Arrays.asList(
                action, drama, comedy, sciFi, thriller, romance
        ));

        Movie speedRush = new Movie(null, "Speed Rush", 1994, 116, null,
                List.of(keanu), List.of(action));
        Movie cityOfHope = new Movie(null, "City of Hope", 1995, 128, null,
                List.of(denzel, merylStreep), List.of(drama));
        Movie laughOutLoud = new Movie(null, "Laugh Out Loud", 1999, 98, null,
                List.of(emma), List.of(comedy));
        Movie starlightOdyssey = new Movie(null, "Starlight Odyssey", 2001, 134, null,
                List.of(scarlett, tomHanks, leonardo), List.of(sciFi, action));
        Movie silentWitness = new Movie(null, "Silent Witness", 2003, 121, null,
                List.of(natalie), List.of(thriller));
        Movie heartsInParis = new Movie(null, "Hearts in Paris", 2004, 105, null,
                List.of(ryan, emma), List.of(romance, drama));
        Movie neonSkies = new Movie(null, "Neon Skies", 2005, 112, null,
                List.of(robert, chris), List.of(sciFi));
        Movie courtroomEchoes = new Movie(null, "Courtroom Echoes", 2006, 129, null,
                List.of(merylStreep, tomHanks), List.of(drama));
        Movie metroMayhem = new Movie(null, "Metro Mayhem", 2007, 109, null,
                List.of(keanu, scarlett), List.of(action));
        Movie galaxyDrift = new Movie(null, "Galaxy Drift", 2008, 141, null,
                List.of(chris, brie), List.of(sciFi, thriller));
        Movie midnightLedger = new Movie(null, "Midnight Ledger", 2010, 118, null,
                List.of(denzel), List.of(thriller));
        Movie heartsStillBeat = new Movie(null, "Hearts Still Beat", 2011, 107, null,
                List.of(emma, ryan), List.of(romance));
        Movie quantumBridge = new Movie(null, "Quantum Bridge", 2012, 143, null,
                List.of(robert, scarlett, chris), List.of(sciFi, action));
        Movie standTall = new Movie(null, "Stand Tall", 2013, 124, null,
                List.of(merylStreep, florence), List.of(drama));
        Movie comicRelief = new Movie(null, "Comic Relief", 2014, 101, null,
                List.of(tomHanks), List.of(comedy));
        Movie doubleCross = new Movie(null, "Double Cross", 2015, 119, null,
                List.of(scarlett, denzel), List.of(thriller, action));
        Movie ringsOfOrion = new Movie(null, "Rings of Orion", 2016, 136, null,
                List.of(zendaya, tomHolland), List.of(sciFi));
        Movie streetJustice = new Movie(null, "Street Justice", 2018, 114, null,
                List.of(chris, keanu), List.of(action));
        Movie cafeAuLait = new Movie(null, "Cafe Au Lait", 2019, 104, null,
                List.of(ryan, emma, florence), List.of(romance, comedy));
        Movie tomorrowsDawn = new Movie(null, "Tomorrow’s Dawn", 2020, 127, null,
                List.of(natalie, zendaya), List.of(drama, romance));
        Movie homeworld = new Movie(null, "Homeworld", 2021, 139, null,
                List.of(brie, zendaya), List.of(sciFi, romance));
        Movie sunsetBoulevarders = new Movie(null, "Sunset Boulevarders", 2022, 110, null,
                List.of(tomHanks, merylStreep), List.of(comedy, drama));
        Movie pulse = new Movie(null, "Pulse", 2023, 122, null,
                List.of(denzel, scarlett, florence), List.of(thriller));
        Movie orbitsEnd = new Movie(null, "Orbit’s End", 2024, 145, null,
                List.of(robert, brie, zendaya), List.of(sciFi, action));

        movieRepository.saveAll(Arrays.asList(
                speedRush, cityOfHope, laughOutLoud, starlightOdyssey, silentWitness,
                heartsInParis, neonSkies, courtroomEchoes, metroMayhem, galaxyDrift,
                midnightLedger, heartsStillBeat, quantumBridge, standTall, comicRelief,
                doubleCross, ringsOfOrion, streetJustice, cafeAuLait, tomorrowsDawn,
                homeworld, sunsetBoulevarders, pulse, orbitsEnd
        ));
    }
}
