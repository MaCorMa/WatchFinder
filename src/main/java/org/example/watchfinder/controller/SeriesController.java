package org.example.watchfinder.controller;

import org.example.watchfinder.model.Series;
import org.example.watchfinder.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @GetMapping("/getall")
    public ResponseEntity<List<Series>> showAll() {
        return new ResponseEntity<>(seriesService.showAll(), HttpStatus.OK);
    }
}
