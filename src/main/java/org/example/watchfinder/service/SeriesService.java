package org.example.watchfinder.service;

import org.example.watchfinder.model.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesService {
    List<Series> showAll();
    Optional<List<Series>> findByTitleContains(String title);
    Optional<List<Series>> findByGenres(List<String> genres);
    Optional<Series> findById(String id);
}
