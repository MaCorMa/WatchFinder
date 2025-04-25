package org.example.watchfinder.controller;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.Movie;
import org.example.watchfinder.model.Series;
import org.example.watchfinder.model.User;
import org.example.watchfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /*@GetMapping("/getprofile")
    public ResponseEntity<User> getProfile() {

    }*/

    @PostMapping("/addtolist")
    public ResponseEntity<?> handleAddToList(@RequestBody Item item, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated")); // Devuelve JSON
        }
        String username = authentication.getName();

        // Llama a tu método del servicio
        boolean success = userService.addItem(username, item);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Failed to add item"));
        }
    }

    @GetMapping("/getfavmovies")
    public ResponseEntity<List<Movie>> getFavMovies(Authentication auth) {
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(user.getFavMovies());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
        }

    }

    @GetMapping("/getfavseries")
    public ResponseEntity<List<Series>> getFavSeries(Authentication auth) {
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.status(HttpStatus.OK).body(user.getFavSeries());
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }   
}

