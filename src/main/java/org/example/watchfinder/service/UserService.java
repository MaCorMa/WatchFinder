package org.example.watchfinder.service;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User updateUser(User user);
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean addItem(String name, Item item);
    boolean removeItem(String name, Item item);
    List<Movie> getMovieRecommendations(String username);

    // --- NUEVO MÉTODO DE RECOMENDACIÓN PARA SERIES ---
    List<Series> getSeriesRecommendations(String username);
}
