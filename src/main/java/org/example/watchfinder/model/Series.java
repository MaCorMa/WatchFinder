package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "Series")
public class Series {

    @Id
    private String _id;
    private String Title;
    private String Year;
    private String ReleaseDate;
    private String EndDate;
    private String Status;
    private String Seasons;
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
    private List<String> Providers;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getProviders() {
        return Providers;
    }

    public void setProviders(List<String> providers) {
        Providers = providers;
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

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSeasons() {
        return Seasons;
    }

    public void setSeasons(String seasons) {
        Seasons = seasons;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(_id, series._id) && Objects.equals(Title, series.Title) && Objects.equals(Year, series.Year) && Objects.equals(ReleaseDate, series.ReleaseDate) && Objects.equals(EndDate, series.EndDate) && Objects.equals(Status, series.Status) && Objects.equals(Seasons, series.Seasons) && Objects.equals(Director, series.Director) && Objects.equals(Country, series.Country) && Objects.equals(Plot, series.Plot) && Objects.equals(Runtime, series.Runtime) && Objects.equals(Ratings, series.Ratings) && Objects.equals(Genres, series.Genres) && Objects.equals(Languages, series.Languages) && Objects.equals(Cast, series.Cast) && Objects.equals(Rated, series.Rated) && Objects.equals(Awards, series.Awards) && Objects.equals(Poster, series.Poster) && Objects.equals(Url, series.Url) && Objects.equals(Providers, series.Providers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, Title, Year, ReleaseDate, EndDate, Status, Seasons, Director, Country, Plot, Runtime, Ratings, Genres, Languages, Cast, Rated, Awards, Poster, Url, Providers);
    }
}