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
    
    private final UserService userService;
    private final MandiService mandiService;

    public MandiController(MandiService mandiService, UserService userService) {
        this.mandiService = mandiService;
        this.userService = userService;
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

}
