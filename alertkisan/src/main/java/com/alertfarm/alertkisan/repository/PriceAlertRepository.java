package com.alertfarm.alertkisan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alertfarm.alertkisan.models.PriceAlert;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    @Query("SELECT p FROM PriceAlert p WHERE p.user.id = :userId")
    List<PriceAlert> findByUserId(Long userId);
    
}
