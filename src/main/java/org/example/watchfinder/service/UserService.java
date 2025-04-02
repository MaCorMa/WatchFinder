package org.example.watchfinder.service;

import org.example.watchfinder.model.User;

public interface UserService {
    public User registerUser(User user);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}
