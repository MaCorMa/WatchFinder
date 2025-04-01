package org.example.watchfinder.repository;

import org.example.watchfinder.model.Series;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeriesRepository extends MongoRepository<Series, String> {
}
