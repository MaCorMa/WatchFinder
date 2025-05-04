package org.example.watchfinder.service;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.model.User;
import org.example.watchfinder.repository.MovieRepository;
import org.example.watchfinder.repository.SeriesRepository;
import org.example.watchfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SeriesRepository seriesRepository;

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
                            if (!user.getLikedMovies().contains(movie)) {
                                user.addToLikedMovies(movie);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }
                        case "disliked":
                            if (!user.getDislikedMovies().contains(movie)) {
                                user.addToDislikedMovies(movie);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }

                        case "seen":
                            if (!user.getSeenMovies().contains(movie)) {
                                user.addToSeenMovies(movie);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }

                        case "fav":
                            if (!user.getFavMovies().contains(movie)) {
                                user.addToFavMovies(movie);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }

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
                            if (!user.getLikedSeries().contains(series)) {
                                user.addToLikedSeries(series);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }
                        case "disliked":
                            if (!user.getDislikedSeries().contains(series)) {
                                user.addToDislikedSeries(series);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }
                        case "seen":
                            if (!user.getSeenSeries().contains(series)) {
                                user.addToSeenSeries(series);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }
                        case "fav":
                            if (!user.getFavSeries().contains(series)) {
                                user.addToFavSeries(series);
                                userRepository.save(user);
                                return true;
                            } else {
                                return false;
                            }
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
    public boolean removeItem(String name, Item item) {
        Optional<User> opt = userRepository.findByUsername(name);
        if (opt.isPresent()) {
            User user = opt.get();

            if (item.getType().equals("movie")) {
                Optional<Movie> optMovie = movieRepository.findBy_id(item.getId());
                if (optMovie.isPresent()) {
                    Movie movie = optMovie.get();

                    switch (item.getState()) {
                        case "seen":
                            user.removeFromSeenMovies(movie);
                            userRepository.save(user);
                            return true;
                        case "fav":
                            user.removeFromFavMovies(movie);
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
                        case "seen":
                            user.removeFromSeenSeries(series);
                            userRepository.save(user);
                            return true;
                        case "fav":
                            user.removeFromFavSeries(series);
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


}
