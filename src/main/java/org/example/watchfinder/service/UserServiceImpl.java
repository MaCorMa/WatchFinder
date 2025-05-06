package org.example.watchfinder.service;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.PasswordResetToken;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.model.User;
import org.example.watchfinder.repository.MovieRepository;
import org.example.watchfinder.repository.PasswordResetTokenRepository;
import org.example.watchfinder.repository.SeriesRepository;
import org.example.watchfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SeriesRepository seriesRepository;

    //Para el reset de la contraseña
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername())) {
            return null;
        } else {
            userRepository.save(user);
            return user;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String mail) {return userRepository.findByEmail(mail);}

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean addItem(String name, Item item) {
        Optional<User> opt = userRepository.findByUsername(name);
        if (opt.isPresent()) {
            User user = opt.get();

            if (item.getType().equals("movie")) {
                Optional<Movie> optMovie = movieRepository.findBy_id(item.getId());
                if (optMovie.isPresent()) {
                    Movie movie = optMovie.get();

                    switch (item.getState()) {
                        case "liked":
                            user.addToLikedMovies(movie);
                            userRepository.save(user);
                            return true;
                        case "disliked":
                            user.addToDislikedMovies(movie);
                            userRepository.save(user);
                            return true;
                        case "seen":
                            user.addToSeenMovies(movie);
                            userRepository.save(user);
                            return true;
                        case "fav":
                            user.addToFavMovies(movie);
                            userRepository.save(user);
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            }
            if (item.getType().equals("series")) {
                Optional<Series> optSeries = seriesRepository.findBy_id(item.getId());
                if (optSeries.isPresent()) {
                    Series series = optSeries.get();

                    switch (item.getState()) {
                        case "liked":
                            user.addToLikedSeries(series);
                            userRepository.save(user);
                            return true;
                        case "disliked":
                            user.addToDislikedSeries(series);
                            userRepository.save(user);
                            return true;
                        case "seen":
                            user.addToSeenSeries(series);
                            userRepository.save(user);
                            return true;
                        case "fav":
                            user.addToFavSeries(series);
                            userRepository.save(user);
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user.getUsername());
        passwordTokenRepository.save(myToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        if (passToken == null) {
            return false;
        }

        Instant now = Instant.now();
        Instant expiry = passToken.getExpiryDate().toInstant();

        if (expiry.isBefore(now) || expiry.equals(now)) {
            passwordTokenRepository.delete(passToken);   //limpia token si ha expirado
            return false;
        }
        return true;
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        return passToken == null ? Optional.empty() : userRepository.findById(passToken.getUserId());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateProfileImageUrl(String username, String imageUrl) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);
        }
    }

    public boolean deleteUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }
}
