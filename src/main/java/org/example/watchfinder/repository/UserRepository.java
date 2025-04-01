package org.example.watchfinder.repository;

import org.example.watchfinder.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    public boolean existsByEmail(String email);
    public boolean existsByNick(String username);
    public User findByNick(String nick);
    public User findByEmail(String email);
}
