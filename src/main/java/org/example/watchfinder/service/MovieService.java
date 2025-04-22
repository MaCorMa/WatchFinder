package org.example.watchfinder.service;

import org.example.watchfinder.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> showAll();
    Optional<Movie> findByTitle(String title);
    Optional<List<Movie>> findByGenresContains(String genre);
}
