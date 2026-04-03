package com.alertfarm.alertkisan.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.alertfarm.alertkisan.models.User;
import com.alertfarm.alertkisan.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        log.info("Creating user with email: {}", user.getEmail());
        // check duplicate email
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Attempt to create user with duplicate email: {}", user.getEmail());
            throw new RuntimeException("Email already exists");
        }
        // TODO: hash password later
        userRepository.save(user);
        log.info("User created successfully: {}", user.getEmail());
    }

    public java.util.List<User> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
}