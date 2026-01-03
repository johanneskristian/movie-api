package com.kood.movieapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SQLiteSchemaMigrator implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SQLiteSchemaMigrator.class);

    private final JdbcTemplate jdbcTemplate;

    public SQLiteSchemaMigrator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            ensureMovieReleaseYearColumn();
            ensureMovieReleaseDateColumn();
        } catch (Exception ex) {
            log.warn("SQLite schema migration skipped or failed: {}", ex.getMessage());
        }
    }

    private void ensureMovieReleaseYearColumn() {
        List<String> cols = jdbcTemplate.query(
                "PRAGMA table_info(movie)",
                (rs, rowNum) -> rs.getString("name")
        );

        if (cols == null) return;

        boolean hasReleaseYear = cols.stream().anyMatch("release_year"::equalsIgnoreCase);
        if (!hasReleaseYear) {
            log.info("[SQLiteSchemaMigrator] Adding missing column movie.release_year ...");
            jdbcTemplate.execute("ALTER TABLE movie ADD COLUMN release_year INTEGER");
            log.info("[SQLiteSchemaMigrator] Column movie.release_year added");
        } else {
            log.debug("[SQLiteSchemaMigrator] Column movie.release_year already exists");
        }
    }

    private void ensureMovieReleaseDateColumn() {
        List<String> cols = jdbcTemplate.query(
                "PRAGMA table_info(movie)",
                (rs, rowNum) -> rs.getString("name")
        );

        if (cols == null) return;

        boolean hasReleaseDate = cols.stream().anyMatch("release_date"::equalsIgnoreCase);
        if (!hasReleaseDate) {
            log.info("[SQLiteSchemaMigrator] Adding missing column movie.release_date ...");
            jdbcTemplate.execute("ALTER TABLE movie ADD COLUMN release_date TEXT");
            log.info("[SQLiteSchemaMigrator] Column movie.release_date added");
        } else {
            log.debug("[SQLiteSchemaMigrator] Column movie.release_date already exists");
        }
    }
}
