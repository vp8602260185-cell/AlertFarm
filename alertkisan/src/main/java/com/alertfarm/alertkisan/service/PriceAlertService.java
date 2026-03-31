package com.alertfarm.alertkisan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alertfarm.alertkisan.models.PriceAlert;
import com.alertfarm.alertkisan.repository.PriceAlertRepository;

@Service
public class PriceAlertService {
    
    @Autowired
    private  PriceAlertRepository alertRepository;

    public PriceAlert saveAlert(PriceAlert alert) {
        alert.setActive(true); 
        alert.setCreatedAt(java.time.LocalDateTime.now().toString());
        alertRepository.save(alert);
        return alert;
    }

    public List<PriceAlert> findByUserId(Long userId) {
        return alertRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        alertRepository.deleteById(id);
    }
}
