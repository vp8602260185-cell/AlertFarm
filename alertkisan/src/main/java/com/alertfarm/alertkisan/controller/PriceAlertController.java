package com.alertfarm.alertkisan.controller;

import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import com.alertfarm.alertkisan.models.PriceAlert;
import com.alertfarm.alertkisan.service.PriceAlertService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/alert")
@Slf4j
public class PriceAlertController {

    @Autowired
    private PriceAlertService priceAlertService;

    @PostMapping("/save")
    public PriceAlert saveAlert(@RequestBody PriceAlert alert) {
        log.info("Received alert: {}", alert);
        return priceAlertService.saveAlert(alert);
    }

    @GetMapping("/user/{userId}")
    public List<PriceAlert> getUserAlerts(@PathVariable Long userId) {
        log.info("Fetching alerts for userId: {}", userId);
        return priceAlertService.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        log.info("Deleting alert with id: {}", id);
        priceAlertService.deleteById(id);
    }
}
