package com.alertfarm.alertkisan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.alertfarm.alertkisan.service.UserService;
import com.alertfarm.alertkisan.models.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public Map<String, String> landingPage() {
        return Map.of("message", "Welcome to AlertKisan!");
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> addUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        
        return ResponseEntity
                .status(201)
                .body(Map.of("message", "User created successfully!"));
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, List<User>>>getUsers() {
        return ResponseEntity
                .status(200)
                .body(Map.of("data",userService.findAll()));
    }
}