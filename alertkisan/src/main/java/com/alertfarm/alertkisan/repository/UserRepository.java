package com.alertfarm.alertkisan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alertfarm.alertkisan.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}