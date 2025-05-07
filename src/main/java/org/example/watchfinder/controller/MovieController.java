package org.example.watchfinder.controller;


import org.apache.coyote.Response;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/getall")
    public ResponseEntity<List<Movie>> showAll(){
        return new ResponseEntity<>(movieService.showAll(), HttpStatus.OK);
    }

    @GetMapping("/getbygenre")
    public ResponseEntity<List<Movie>> findByGenres(@RequestParam List<String> genres){
        Optional<List<Movie>> movieList = movieService.findByGenres(genres);
        if(movieList.isPresent()){
            return new ResponseEntity<>(movieList.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getbytitle")
    public ResponseEntity<List<Movie>> findByTitleContains(@RequestParam String title){
        Optional<List<Movie>> optionalMovie = movieService.findByTitleContains(title);
        if(optionalMovie.isPresent()){
            return new ResponseEntity<>(optionalMovie.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getbyid")
    public ResponseEntity<Movie> findById(@RequestParam String id){
        Optional<Movie> movieOpt = movieService.findById(id);
        if(movieOpt.isPresent()){
            return new ResponseEntity<>(movieOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
