package org.example.watchfinder.controller;


import org.apache.coyote.Response;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/getall")
    public ResponseEntity<List<Movie>> showAll(){
        return new ResponseEntity<>(movieService.showAll(), HttpStatus.OK);
    }

}
