package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Movies")
public class Movie {

    @Id
    private String _id;
    private String Title;
    private int Year;
    private String ReleaseDate;
    private String Director;
    private String Country;
    private String Plot;
    private String Runtime;
    private List<String> Ratings;
    private List<String> Genres;
    private List<String> Languages;
    private List<String> Cast;
    private String Rated;
    private String Awards;
    private String Poster;
    private String Url;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public List<String> getRatings() {
        return Ratings;
    }

    public void setRatings(List<String> ratings) {
        Ratings = ratings;
    }

    public List<String> getGenres() {
        return Genres;
    }

    public void setGenres(List<String> genres) {
        Genres = genres;
    }

    public List<String> getLanguages() {
        return Languages;
    }

    public void setLanguages(List<String> languages) {
        Languages = languages;
    }

    public List<String> getCast() {
        return Cast;
    }

    public void setCast(List<String> cast) {
        Cast = cast;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }
}
