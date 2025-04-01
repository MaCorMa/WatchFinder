package org.example.watchfinder.service;

import org.example.watchfinder.model.User;
import org.example.watchfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByNick(user.getNick())) {
            return null;
        } else {
            userRepository.save(user);
            return user;
        }
    }
}
