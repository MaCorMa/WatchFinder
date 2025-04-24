package org.example.watchfinder.controller;

import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @GetMapping("/getall")
    public ResponseEntity<List<Series>> showAll() {
        return new ResponseEntity<>(seriesService.showAll(), HttpStatus.OK);
    }

    @GetMapping("/getbygenre")
    public ResponseEntity<List<Series>> findByGenres(@RequestParam List<String> genres){
        Optional<List<Series>> seriesList = seriesService.findByGenres(genres);
        if(seriesList.isPresent()){
            return new ResponseEntity<>(seriesList.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getbytitle")
    public ResponseEntity<List<Series>> findByTitleContains(@RequestParam String title){
        Optional<List<Series>> optionalSeries = seriesService.findByTitleContains(title);
        if(optionalSeries.isPresent()){
            return new ResponseEntity<>(optionalSeries.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getbyid")
    public ResponseEntity<Series> findById(@RequestParam String id){
        Optional<Series> seriesOpt = seriesService.findById(id);
        if(seriesOpt.isPresent()){
            return new ResponseEntity<>(seriesOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
