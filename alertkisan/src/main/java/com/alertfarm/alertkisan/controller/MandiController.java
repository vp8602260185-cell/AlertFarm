package com.alertfarm.alertkisan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.service.MandiService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/mandi")
@Slf4j
public class MandiController {

    @Autowired
    private MandiService mandiService;

    @PostMapping("/")
    public String postMethodName() {
        log.info("Fetching and storing mandi data");
        mandiService.fetchAndSaveMandiData();
        log.info("Data fetched and stored successfully");
        return "Data Fetched and Stored Successfully!";
    }

    @GetMapping("/")
    public List<MandiPrice> fetchMandiPrice() {
        log.info("Fetching all mandi prices");
        return mandiService.getAllMandiPrices();
    }

    @GetMapping("/total_records")
    public TotalRecords getMethodName() {
        log.info("Fetching total records");
        return mandiService.fetchTotalCount();
    }

    @GetMapping("/states")
    public List<String> getStates() {
        log.info("Fetching distinct states");
        return mandiService.findDistinctStates();
    }

    @GetMapping("/districts")
    public List<String> getDistricts(@RequestParam String state) {
        log.info("Fetching districts for state: {}", state);
        return mandiService.findDistinctDistrictsByState(state);
    }

    @GetMapping("/commodities")
    public List<String> getCommodities(@RequestParam String state, @RequestParam String district) {
        log.info("Fetching commodities for state: {}, district: {}", state, district);
        return mandiService.findDistinctCommoditiesByStateAndDistrict(state, district);
    }

    @GetMapping("/commoditie_price")
    public List<MandiPrice> search(
            @RequestParam String state, 
            @RequestParam String district, 
            @RequestParam String commodity) {
        return mandiService.getLatestMandiPrices(state, district, commodity);
    }

    @GetMapping("/history")
    public List<MandiPrice> getRecentHistory(
            @RequestParam String state, 
            @RequestParam String district, 
            @RequestParam String commodity,
            @RequestParam(required = false) Integer days) {
        if(days == null || days <= 0) {
            days = 7;
        }
        return mandiService.getRecentHistory(state, district, commodity, days);
    }
    
}
