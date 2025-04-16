package org.example.watchfinder.service;

import org.example.watchfinder.dto.Item;
import org.example.watchfinder.model.User;

import java.util.Optional;

public interface UserService {
    public User updateUser(User user);
    public User registerUser(User user);
    public Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public boolean addItem(String name, Item item);
}
