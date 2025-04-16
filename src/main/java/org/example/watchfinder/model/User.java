package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "Users")
public class User implements UserDetails {

    @Id
    private String _id;
    private String name;
    private String username;
    private String password;
    private String email;
    private List<Movie> likedMovies = new ArrayList<>();
    private List<Movie> dislikedMovies = new ArrayList<>();
    private List<Series> likedSeries = new ArrayList<>();
    private List<Series> dislikedSeries = new ArrayList<>();
    private List<Movie> seenMovies = new ArrayList<>();
    private List<Series> seenSeries = new ArrayList<>();
    private List<Movie> favMovies = new ArrayList<>();
    private List<Series> favSeries = new ArrayList<>();
    private List<String> achievements = new ArrayList<>();
    private Set<String> roles;

    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toList());
    }

    public void addToLikedMovies(Movie movie){
        this.likedMovies.add(movie);
    }
    public void addToLikedSeries(Series series){
        this.likedSeries.add(series);
    }

    public void addToDislikedMovies(Movie movie){
        this.dislikedMovies.add(movie);
    }
    public void addToDislikedSeries(Series series){
        this.dislikedSeries.add(series);
    }

    public void addToSeenMovies(Movie movie){
        this.seenMovies.add(movie);
    }
    public void addToSeenSeries(Series series){
        this.seenSeries.add(series);
    }

    public void addToFavMovies(Movie movie){
        this.favMovies.add(movie);
    }
    public void addToFavSeries(Series series){
        this.favSeries.add(series);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

