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
    private Set<String> roles;
    private String profileImageUrl;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }


    public void addToLikedMovies(Movie movie) {
        this.likedMovies.add(movie);
    }

    public void addToLikedSeries(Series series) {
        this.likedSeries.add(series);
    }



    public void addToDislikedMovies(Movie movie) {
        this.dislikedMovies.add(movie);
    }

    public void addToDislikedSeries(Series series) {
        this.dislikedSeries.add(series);
    }



    public void addToSeenMovies(Movie movie) {
        this.seenMovies.add(movie);
    }

    public void addToSeenSeries(Series series) {
        this.seenSeries.add(series);
    }



    public void addToFavMovies(Movie movie) {
        this.favMovies.add(movie);
    }

    public void addToFavSeries(Series series) {
        this.favSeries.add(series);
    }



    public void removeFromSeenMovies(Movie movie) {
        this.seenMovies.remove(movie);
    }

    public void removeFromSeenSeries(Series series) {
        this.seenSeries.remove(series);
    }
    public void removeFromFavMovies(Movie movie) {
        this.favMovies.remove(movie);
    }
    public void removeFromFavSeries(Series series) {
        this.favSeries.remove(series);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {this.username = username;}

    public void setPassword(String password) {this.password = password; }

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

