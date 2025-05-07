package org.example.watchfinder.service;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User updateUser(User user);
    public User registerUser(User user);
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String mail);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public boolean addItem(String name, Item item);
    boolean removeItem(String name, Item item);

    //Para reset contraseña
    void createPasswordResetToken(User user, String token);
    boolean validatePasswordResetToken(String token);
    Optional<User> getUserByPasswordResetToken(String token);
    void changePassword(User user, String newPassword);
    void updateProfileImageUrl(String username, String imageUrl);


    //Delete user
    boolean deleteUser(String userName);


    List<Movie> getMovieRecommendations(String username);
    List<Series> getSeriesRecommendations(String username);
}
