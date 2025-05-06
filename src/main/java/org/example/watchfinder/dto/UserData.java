package org.example.watchfinder.dto;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;

import java.util.List;

public class UserData {

    private String username;
    private String email;
    private String profileImageUrl;
    private List<Movie> likedMovies;
    private List<Movie> dislikedMovies;
    private List<Series> likedSeries;
    private List<Series> dislikedSeries;
    private List<Movie> seenMovies;
    private List<Series> seenSeries;
    private List<Movie> favMovies;
    private List<Series> favSeries;

    public UserData() {
    }

    public UserData(String username, String email, String profileImageUrl) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public UserData(String username, String email, String profileImageUrl, List<Movie> likedMovies, List<Movie> dislikedMovies, List<Series> likedSeries, List<Series> dislikedSeries, List<Movie> seenMovies, List<Series> seenSeries, List<Movie> favMovies, List<Series> favSeries) {
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.likedMovies = likedMovies;
        this.dislikedMovies = dislikedMovies;
        this.likedSeries = likedSeries;
        this.dislikedSeries = dislikedSeries;
        this.seenMovies = seenMovies;
        this.seenSeries = seenSeries;
        this.favMovies = favMovies;
        this.favSeries = favSeries;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
}
