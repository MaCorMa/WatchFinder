package org.example.watchfinder.controller;

import org.example.watchfinder.model.User;
import org.example.watchfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if(userService.registerUser(user) != null) {
            userService.registerUser(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not registered", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody User user) {

    }
}
