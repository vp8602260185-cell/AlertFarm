package com.alertfarm.alertkisan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.service.FetchHistoricService;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/fetch_historic")
@Slf4j
public class FetchHistoricController {

    @Autowired
    private FetchHistoricService fetchHistoricService;

    @PostMapping("/")
    public ResponseEntity<Integer> fetchHistoricRecord(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        TotalRecords data=fetchHistoricService.fetchTotalCount(state, district, commodity);
        int total_records=data.totalRecords();
        log.info("Total Records for {} in {}, {}: {}", commodity, district, state, total_records);
        return ResponseEntity.status(200).body(total_records);
    }

    @PostMapping("/save")
    public  ResponseEntity<String>  postMethodName(@RequestParam String state, @RequestParam String district, @RequestParam String commodity) {
        log.info("Fetching and saving historic data for {}, {}, {}", state, district, commodity);
        fetchHistoricService.fetchAndSaveMandiData(state, district, commodity);
        return ResponseEntity.status(200).body("Data Fetched and Stored Successfully!") ;
    }

    @PostMapping("/storeForAll")
    // @EventListener(ApplicationReadyEvent.class)
    public String storeHistoricForAll() {
        int total_records=fetchHistoricService.storeHistoricRecordForAll();
        log.info("Total Records Fetched and Stored for All: {}", total_records);
        return "Total Records Fetched and Stored Successfully: "+total_records;
    }

}
