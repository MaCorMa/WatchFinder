package org.example.watchfinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class Movie {

    @Id
    private String _id;
    private String title;
    private int year;
    private String releaseDate;
    private String director;
    private String country;
    private String plot;
    private String runtime;
    private List<String> genres;
    private List<String> languages;
    private List<String> cast;
    private List<String> ratings;
    private String rated;
    private String awards;

}
