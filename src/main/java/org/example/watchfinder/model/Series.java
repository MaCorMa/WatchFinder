package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Series {

    @Id
    private String _id;
    private String title;
    private int year;
    private String releaseDate;
    private String endDate;
    private String status;
    private String seasons;
    private String director;
    private String country;
    private String plot;
    private String runtime;
    private List<String> ratings;
    private List<String> genres;
    private List<String> languages;
    private List<String> cast;
    private String rated;
    private String awards;
}
