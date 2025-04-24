package org.example.watchfinder.repository;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends MongoRepository<Series, String> {
    Optional<Series> findBy_id(String id);
    @Query("{ 'Title': { $regex: ?0, $options: 'i' } }")
    Optional<List<Series>> findByTitleContains(String title);
    @Query("{ 'Genres': { $all: ?0 } }")
    Optional<List<Series>> findByGenres(List<String> genres);
}
