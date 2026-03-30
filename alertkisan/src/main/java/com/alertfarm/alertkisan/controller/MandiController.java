package com.alertfarm.alertkisan.controller;

import com.alertfarm.alertkisan.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.service.MandiService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/mandi")
public class MandiController {
    
    private final MandiService mandiService;

    public MandiController(MandiService mandiService) {
        this.mandiService = mandiService;
    }

    @PostMapping("/")
    public String postMethodName() {
        mandiService.fetchAndSaveMandiData();;
        return "Data Fetched and Stored Successfully!";
    }

    @GetMapping("/")
    public List<MandiPrice> fetchMandiPrice() {
        return mandiService.getAllMandiPrices();
    }
    
    @GetMapping("/total_records")
    public TotalRecords getMethodName() {
        return mandiService.fetchTotalCount();
    }

    @GetMapping("/states")
    public List<String> getStates() {
        return mandiService.findDistinctStates();
    }

    @GetMapping("/districts")
    public List<String> getDistricts(@RequestParam String state) {
        return mandiService.findDistinctDistrictsByState(state);
    }

    @GetMapping("/commodities")
    public List<String> getCommodities(@RequestParam String state, @RequestParam String district) {
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
