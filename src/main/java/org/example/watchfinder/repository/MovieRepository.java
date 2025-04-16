package org.example.watchfinder.repository;

import org.example.watchfinder.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findBy_id(String id);
}
