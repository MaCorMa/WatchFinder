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
}
