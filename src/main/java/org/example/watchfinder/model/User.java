package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class User {

    @Id
    private String _id;
    private String name;
    private String password;
    private String email;
    private List<Movie> likedMovies;
    private List<Movie> dislikedMovies;
    private List<Series> likedSeries;
    private List<Series> dislikedSeries;
    private List<Movie> seenMovies;
    private List<Series> seenSeries;
    private List<Movie> favMovies;
    private List<Series> favSeries;
    private List<String> achievements;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<Movie> getDislikedMovies() {
        return dislikedMovies;
    }

    public void setDislikedMovies(List<Movie> dislikedMovies) {
        this.dislikedMovies = dislikedMovies;
    }

    public List<Series> getLikedSeries() {
        return likedSeries;
    }

    public void setLikedSeries(List<Series> likedSeries) {
        this.likedSeries = likedSeries;
    }

    public List<Series> getDislikedSeries() {
        return dislikedSeries;
    }

    public void setDislikedSeries(List<Series> dislikedSeries) {
        this.dislikedSeries = dislikedSeries;
    }

    public List<Movie> getSeenMovies() {
        return seenMovies;
    }

    public void setSeenMovies(List<Movie> seenMovies) {
        this.seenMovies = seenMovies;
    }

    public List<Series> getSeenSeries() {
        return seenSeries;
    }

    public void setSeenSeries(List<Series> seenSeries) {
        this.seenSeries = seenSeries;
    }

    public List<Movie> getFavMovies() {
        return favMovies;
    }

    public void setFavMovies(List<Movie> favMovies) {
        this.favMovies = favMovies;
    }

    public List<Series> getFavSeries() {
        return favSeries;
    }

    public void setFavSeries(List<Series> favSeries) {
        this.favSeries = favSeries;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }
}
