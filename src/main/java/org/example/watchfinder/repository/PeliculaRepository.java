package org.example.watchfinder.repository;

import org.example.watchfinder.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PeliculaRepository extends MongoRepository<Movie, String> {
}
