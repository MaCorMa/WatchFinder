package org.example.watchfinder.service;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> showAll();
    Optional<List<Movie>> findByTitleContains(String title);
    Optional<List<Movie>> findByGenres(List<String> genres);
    Optional<Movie> findById(String id);
}
