package org.example.watchfinder.service;

import org.example.watchfinder.model.Series;
import org.example.watchfinder.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesServiceImpl implements SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Override
    public List<Series> showAll() {
        return seriesRepository.findAll();
    }

    @Override
    public Optional<List<Series>> findByTitleContains(String title) {
        return seriesRepository.findByTitleContains(title);
    }

    @Override
    public Optional<List<Series>> findByGenres(List<String> genres) {
        return seriesRepository.findByGenres(genres);
    }

    @Override
    public Optional<Series> findById(String id) {
        return seriesRepository.findById(id);
    }


}
