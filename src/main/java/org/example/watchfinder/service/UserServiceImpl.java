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
import java.util.*;
import java.util.stream.Collectors;

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
    public List<Movie> getMovieRecommendations(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return Collections.emptyList(); // Si el usuario no existe, no hay recomendaciones
        }
        User user = userOpt.get();

        // 1. Recopilar IDs de películas a excluir
        Set<String> excludedMovieIds = new HashSet<>();
        if (user.getSeenMovies() != null) {
            user.getSeenMovies().stream().map(Movie::get_id).forEach(excludedMovieIds::add);
        }
        if (user.getFavMovies() != null) {
            user.getFavMovies().stream().map(Movie::get_id).forEach(excludedMovieIds::add);
        }
        if (user.getLikedMovies() != null) { // Asumiendo que User tiene getLikedMovies()
            user.getLikedMovies().stream().map(Movie::get_id).forEach(excludedMovieIds::add);
        }
        if (user.getDislikedMovies() != null) { // Asumiendo que User tiene getDislikedMovies()
            user.getDislikedMovies().stream().map(Movie::get_id).forEach(excludedMovieIds::add);
        }

        // 2. Obtener películas candidatas (no vistas, no fav, no liked, no disliked)
        List<Movie> candidateMovies = movieRepository.findByIdNotIn(new ArrayList<>(excludedMovieIds));
        if (candidateMovies.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. Calcular el perfil de géneros del usuario
        Map<String, Double> userGenreScores = new HashMap<>();
        // Puntos por películas favoritas
        if (user.getFavMovies() != null) {
            for (Movie movie : user.getFavMovies()) {
                if (movie.getGenres() != null) {
                    for (String genre : movie.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) + 2.0);
                    }
                }
            }
        }
        // Puntos por películas que le gustaron
        if (user.getLikedMovies() != null) { // Asumiendo getLikedMovies()
            for (Movie movie : user.getLikedMovies()) {
                if (movie.getGenres() != null) {
                    for (String genre : movie.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) + 1.0);
                    }
                }
            }
        }
        // (Opcional) Restar puntos por películas que no le gustaron
        if (user.getDislikedMovies() != null) { // Asumiendo getDislikedMovies()
            for (Movie movie : user.getDislikedMovies()) {
                if (movie.getGenres() != null) {
                    for (String genre : movie.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) - 1.0);
                    }
                }
            }
        }

        List<ScoredMovie> scoredCandidates = new ArrayList<>();
        for (Movie candidate : candidateMovies) {
            double candidateScore = 0.0;
            if (candidate.getGenres() != null) {
                for (String genre : candidate.getGenres()) {
                    candidateScore += userGenreScores.getOrDefault(genre, 0.0);
                }
            }
            // Solo considerar recomendar si la puntuación es positiva
            if (candidateScore > 0) {
                scoredCandidates.add(new ScoredMovie(candidate, candidateScore));
            }
        }

        if (scoredCandidates.isEmpty()) {
            return Collections.emptyList(); // No hay candidatos con puntuación positiva
        }

        // 5. Ordenar los candidatos por puntuación (de mayor a menor)
        scoredCandidates.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));

        // 6. Extraer las películas ordenadas
        List<Movie> finalRecommendations = scoredCandidates.stream()
                .map(ScoredMovie::getMovie)
                .collect(Collectors.toList());

        // 7. Limitar el número de recomendaciones (ej. las 20 mejores)
        int limit = 5;
        return finalRecommendations.subList(0, Math.min(finalRecommendations.size(), limit));
    }

    private static class ScoredMovie {
        private final Movie movie;
        private final double score;

        public ScoredMovie(Movie movie, double score) {
            this.movie = movie;
            this.score = score;
        }

        public Movie getMovie() {
            return movie;
        }

        public double getScore() {
            return score;
        }
    }



    @Override
    public List<Series> getSeriesRecommendations(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return Collections.emptyList();
        }
        User user = userOpt.get();

        // 1. Recopilar IDs de series a excluir
        Set<String> excludedSeriesIds = new HashSet<>();
        if (user.getSeenSeries() != null) {
            user.getSeenSeries().stream().map(Series::get_id).forEach(excludedSeriesIds::add);
        }
        if (user.getFavSeries() != null) {
            user.getFavSeries().stream().map(Series::get_id).forEach(excludedSeriesIds::add);
        }
        if (user.getLikedSeries() != null) {
            user.getLikedSeries().stream().map(Series::get_id).forEach(excludedSeriesIds::add);
        }
        if (user.getDislikedSeries() != null) {
            user.getDislikedSeries().stream().map(Series::get_id).forEach(excludedSeriesIds::add);
        }

        // 2. Obtener series candidatas
        List<Series> candidateSeries = seriesRepository.findByIdNotIn(new ArrayList<>(excludedSeriesIds));
        if (candidateSeries.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. Calcular el perfil de géneros del usuario para series
        Map<String, Double> userGenreScores = new HashMap<>();
        if (user.getFavSeries() != null) {
            for (Series series : user.getFavSeries()) {
                if (series.getGenres() != null) {
                    for (String genre : series.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) + 2.0);
                    }
                }
            }
        }
        if (user.getLikedSeries() != null) {
            for (Series series : user.getLikedSeries()) {
                if (series.getGenres() != null) {
                    for (String genre : series.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) + 1.0);
                    }
                }
            }
        }
        if (user.getDislikedSeries() != null) {
            for (Series series : user.getDislikedSeries()) {
                if (series.getGenres() != null) {
                    for (String genre : series.getGenres()) {
                        userGenreScores.put(genre, userGenreScores.getOrDefault(genre, 0.0) - 1.0);
                    }
                }
            }
        }

        // 4. Puntuar las series candidatas
        List<ScoredSeries> scoredCandidates = new ArrayList<>();
        for (Series candidate : candidateSeries) {
            double candidateScore = 0.0;
            if (candidate.getGenres() != null) {
                for (String genre : candidate.getGenres()) {
                    candidateScore += userGenreScores.getOrDefault(genre, 0.0);
                }
            }
            if (candidateScore > 0) {
                scoredCandidates.add(new ScoredSeries(candidate, candidateScore));
            }
        }

        if (scoredCandidates.isEmpty()) {
            return Collections.emptyList();
        }

        // 5. Ordenar los candidatos por puntuación
        scoredCandidates.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));

        // 6. Extraer las series ordenadas
        List<Series> finalRecommendations = scoredCandidates.stream()
                .map(ScoredSeries::getSeries)
                .collect(Collectors.toList());

        // 7. Limitar el número de recomendaciones
        int limit = 5;
        return finalRecommendations.subList(0, Math.min(finalRecommendations.size(), limit));
    }

    private static class ScoredSeries {
        private final Series series;
        private final double score;

        public ScoredSeries(Series series, double score) {
            this.series = series;
            this.score = score;
        }

        public Series getSeries() {
            return series;
        }

        public double getScore() {
            return score;
        }
    }
}
