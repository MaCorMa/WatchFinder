package org.example.watchfinder.service;

import org.example.watchfinder.model.Series;
import org.example.watchfinder.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesServiceImpl implements SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Override
    public List<Series> showAll() {
        return seriesRepository.findAll();
    }
}
