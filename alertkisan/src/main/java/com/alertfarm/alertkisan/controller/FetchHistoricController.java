package com.alertfarm.alertkisan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.service.FetchHistoricService;
import com.alertfarm.alertkisan.service.MandiService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/v1/fetch_historic")
public class FetchHistoricController {

    @Autowired
    private  MandiService mandiService;

    @Autowired
    private FetchHistoricService fetchHistoricService;

    @PostMapping("/")
    public ResponseEntity<Integer> fetchHistoricRecord(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        // Logic to fetch historic record from the database
        TotalRecords data=fetchHistoricService.fetchTotalCount(state, district, commodity);
        int total_records=data.totalRecords();
        System.out.println("Total Records for " + commodity + " in " + district + ", " + state + ": " + total_records);
        return ResponseEntity.status(200).body(total_records);//"Historic record fetched successfully!"; 
    }

    @PostMapping("/save")
    public  ResponseEntity<String>  postMethodName(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        fetchHistoricService.fetchAndSaveMandiData(state, district, commodity);
        return ResponseEntity.status(200).body("Data Fetched and Stored Successfully!") ;
    }

    @PostMapping("/storeForAll")
    public String storeHistoricForAll() {
        int total_records=fetchHistoricService.storeHistoricRecordForAll();
        return "Total Records Fetched and Stored Successfully: "+total_records;
    }
    

}
