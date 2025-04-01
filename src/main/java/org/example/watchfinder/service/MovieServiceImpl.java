package org.example.watchfinder.service;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> showAll() {
        return movieRepository.findAll();
    }
}
