package com.alertfarm.alertkisan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alertfarm.alertkisan.models.MandiPrice;

public interface MandiRepository  extends JpaRepository<MandiPrice, Long> {
    
} 
