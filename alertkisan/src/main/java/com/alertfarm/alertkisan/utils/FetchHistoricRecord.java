package com.alertfarm.alertkisan.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.service.MandiService;


@RestController
public class FetchHistoricRecord {

    @Autowired
    private  MandiService mandiService;

    @PostMapping("/api/v1/historic-record")
    public ResponseEntity<Integer> fetchHistoricRecord(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        // Logic to fetch historic record from the database
        TotalRecords data=mandiService.fetchTotalCount(state, district, commodity);
        int total_records=data.totalRecords();
        System.out.println("Total Records for " + commodity + " in " + district + ", " + state + ": " + total_records);
        return ResponseEntity.status(200).body(total_records);//"Historic record fetched successfully!"; 
    }

    @PostMapping("/api/v1/historic-record/save")
    public  ResponseEntity<String>  postMethodName(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        mandiService.fetchAndSaveMandiData(state, district, commodity);
        return ResponseEntity.status(200).body("Data Fetched and Stored Successfully!") ;
    }
    
}
