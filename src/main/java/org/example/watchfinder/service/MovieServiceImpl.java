package org.example.watchfinder.service;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> showAll() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<List<Movie>> findByTitleContains(String title) {
        return movieRepository.findByTitleContains(title);
    }

    @Override
    public Optional<List<Movie>> findByGenres(List<String> genres) {
        return movieRepository.findByGenres(genres);
    }

    @Override
    public Optional<Movie> findById(String id) {
        return movieRepository.findById(id);
    }


}
