package org.example.watchfinder.repository;

import org.example.watchfinder.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findBy_id(String id);

    @Query("{ 'Title': { $regex: ?0, $options: 'i' } }")
    Optional<List<Movie>> findByTitleContains(String title);
    @Query("{ 'Genres': { $all: ?0 } }")
    Optional<List<Movie>> findByGenres(List<String> genres);
    @Query("{ '_id' : { $nin: ?0 } }")
    List<Movie> findByIdNotIn(List<String> excludedIds);

}
