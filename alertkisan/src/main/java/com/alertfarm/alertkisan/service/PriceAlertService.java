package com.alertfarm.alertkisan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.alertfarm.alertkisan.models.PriceAlert;
import com.alertfarm.alertkisan.repository.PriceAlertRepository;

@Service
@Slf4j
public class PriceAlertService {

    @Autowired
    private  PriceAlertRepository alertRepository;

    public PriceAlert saveAlert(PriceAlert alert) {
        alert.setActive(true); 
        alert.setCreatedAt(java.time.LocalDateTime.now().toString());
        log.info("Saving price alert for user: {}", alert.getUser() != null ? alert.getUser().getId() : null);
        alertRepository.save(alert);
        return alert;
    }

    public List<PriceAlert> findByUserId(Long userId) {
        log.info("Finding price alerts for userId: {}", userId);
        return alertRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        log.info("Deleting price alert with id: {}", id);
        alertRepository.deleteById(id);
    }
}
