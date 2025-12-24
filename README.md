Movie API

Overview
- REST API to manage Movies, Genres, and Actors.
- CRUD + PATCH, filtering, pagination, search, and safe deletions.

Quick start
- Requirements: Java 17+, Maven 3.9+
- Run: mvn spring-boot:run (app at http://localhost:8080)
- DB: SQLite file (movie.db). Dialect: org.hibernate.community.dialect.SQLiteDialect

Entities and relations
- Movie ↔ Genre: Many-to-Many
- Movie ↔ Actor: Many-to-Many
- Movie fields: id, title, releaseYear, duration, genres, actors
- Genre fields: id, name
- Actor fields: id, name, birthDate (YYYY-MM-DD)

Endpoints 
- Base URL: http://localhost:8080

Genres
- POST  /api/genres — Create a new genre
  - Body: { "name": "Action" }
  - Success: 201 Created + created Genre JSON
  - Errors: 400 if name is blank
- GET   /api/genres — List all genres
  - Success: 200 OK + array of genres
- GET   /api/genres/{genreId} — Get one genre by ID
  - Success: 200 OK + Genre JSON
  - Errors: 404 if not found
- PATCH /api/genres/{genreId} — Update genre name (partial)
  - Body: { "name": "New Name" }
  - Success: 200 OK + updated Genre JSON
  - Errors: 400 invalid data, 404 not found
- GET   /api/genres/{genreId}/movies — Movies in this genre
  - Success: 200 OK + array of movies
- DELETE /api/genres/{genreId}?force=true — Delete a genre
  - Default delete (no force): 400 if genre has related movies
  - Force delete (force=true): detaches from movies then deletes → 204 No Content

Movies
- POST  /api/movies — Create a new movie
  - Body: { "title":"Inception", "releaseYear":2010, "duration":148, "genres":[{"id":1}], "actors":[{"id":3}] }
  - Success: 201 Created + created Movie JSON
  - Errors: 400 invalid data or unknown IDs
- GET   /api/movies?page=0&size=10 — List movies (paginated)
  - Optional filters: ?genre={genreId} | ?year={releaseYear} | ?actor={actorId}
  - Success: 200 OK + Page wrapper with content array
- GET   /api/movies/search?title={text}&page=0&size=10 — Search by title (case-insensitive)
  - Success: 200 OK + Page of matches
- GET   /api/movies/{movieId} — Get one movie by ID
  - Success: 200 OK + Movie JSON
  - Errors: 404 if not found
- GET   /api/movies/{movieId}/actors — Actors in a movie
  - Success: 200 OK + array of actors
- PATCH /api/movies/{movieId} — Partial update (title, releaseYear, duration, genres, actors)
  - Body: only fields you want to change, e.g. { "duration": 150 }
  - Success: 200 OK + updated Movie JSON
  - Errors: 400 invalid data/IDs, 404 not found
- DELETE /api/movies/{movieId} — Delete a movie
  - Success: 204 No Content
  - Note: accepts ?force=true but not required for movies

Actors
- POST  /api/actors — Create a new actor
  - Body: { "name":"Tom Hanks", "birthDate":"1956-07-09" }
  - Success: 201 Created + created Actor JSON
  - Errors: 400 invalid name or birthDate not in the past
- GET   /api/actors — List actors; filter by name with ?name={text}
  - Success: 200 OK + array of actors
- GET   /api/actors/{actorId} — Get one actor by ID
  - Success: 200 OK + Actor JSON
  - Errors: 404 if not found
- PATCH /api/actors/{actorId} — Partial update (name, birthDate)
  - Body: e.g. { "name": "New Name" }
  - Success: 200 OK + updated Actor JSON
  - Errors: 400 invalid data, 404 not found
- GET   /api/movies?actor={actorId}&page=0&size=10 — Movies starring this actor (paginated)
  - Success: 200 OK + Page of movies
- DELETE /api/actors/{actorId}?force=true — Delete an actor
  - Default delete (no force): 400 if actor is in movies
  - Force delete (force=true): removes actor from movies then deletes → 204 No Content

Request examples
- Create Genre
  {
    "name": "Action"
  }
- Create Actor
  {
    "name": "Tom Hanks",
    "birthDate": "1956-07-09"
  }
- Create Movie (use existing IDs)
  {
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [{ "id": 1 }, { "id": 4 }],
    "actors": [{ "id": 3 }, { "id": 14 }]
  }

Pagination and search
- All movie listings are paged (Page wrapper): ?page=0&size=10
- Title search: GET /api/movies/search?title=matrix&page=0&size=10
 - Pagination rules: page >= 0, 1 <= size <= 100. Invalid values return 400 with a clear error message.

Deletions with relationships
- Default: deleting a Genre/Actor with related movies returns 400.
- Force: add ?force=true to detach relations, then delete (returns 204).

Sample data
- sample-data/genres.json
- sample-data/actors.json
- sample-data/movies.json
Import in order: genres → actors → movies. Adjust IDs in movies.json to match your created IDs if needed.

Errors and validation
- 400: validation/illegal arguments; 404: not found; 500: unexpected.
- Common validations: non-blank title/name, releaseYear not null, birthDate in the past.

Notes
- POST returns the created entity (201 Created).
- GET list endpoints return a Page with content and pagination metadata.
