package com.alertfarm.alertkisan.service;

import org.springframework.stereotype.Service;

import com.alertfarm.alertkisan.models.User;
import com.alertfarm.alertkisan.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {

        // 👉 Business logic here

        // check duplicate email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // TODO: hash password later

        userRepository.save(user);
    }

    public java.util.List<User> findAll() {
        return userRepository.findAll();
    }
}