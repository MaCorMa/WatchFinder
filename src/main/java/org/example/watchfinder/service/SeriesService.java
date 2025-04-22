package org.example.watchfinder.service;

import org.example.watchfinder.model.Series;

import java.util.List;
import java.util.Optional;

public interface SeriesService {
    List<Series> showAll();
    Optional<Series> findByTitle(String title);
    Optional<List<Series>> findByGenresContains(String genre);
}
