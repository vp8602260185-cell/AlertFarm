package com.alertfarm.alertkisan.service;

import com.alertfarm.alertkisan.dto.*;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.repository.MandiRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MandiService {

    @Value("${mandi.api.key}")
    private String apiKey;

    @Value("${mandi.api.url}")
    String url;

    @Value("${mandi.api.format}")
    String format;

    @Value("${mandi.api.limit}")
    int limit;

    private final MandiRepository repository;
    private final RestTemplate restTemplate;


    public MandiService(MandiRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public TotalRecords fetchTotalCount() {
        String url = this.url
                     + apiKey 
                     + "&format="+format
                     + "&limit=1";

        TotalRecords response = restTemplate.getForObject(url, TotalRecords.class);
        if (response != null) {
            System.out.println("Total Records: " + response.totalRecords());
        } else {
            System.out.println("Failed to fetch total records.");
        }
        return response;
    }

    private void fetchBatch(int offset) {
        String url = this.url
                     + apiKey 
                     + "&format=json"
                     + "&limit=" + limit
                     + "&offset=" + offset;

        // Use your main MandiApiResponse DTO here
        MandiApiResponse response = restTemplate.getForObject(url, MandiApiResponse.class);
        List<MandiPrice> entities = new ArrayList<>();
        if (response != null && response.records() != null) {
            for (MandiRecord record : response.records()) {
                MandiPrice entity = this.mapToEntity(record);
                entities.add(entity);
            }
        }
        repository.saveAll(entities);
    }

    public void fetchAndSaveMandiData() {
        int total = fetchTotalCount().totalRecords();
        System.out.println("Total Records: " + total);  
        for (int offset = 0; offset < total; offset += limit) {
            System.out.println("Fetching batch with offset: " + offset);
            fetchBatch(offset);
        }
    }

    private MandiPrice mapToEntity(MandiRecord record) {
        return MandiPrice.builder()
            .id(MandiPrice.createId(record)) // Set the composite key
            .variety(record.variety())
            .minPrice(record.minPrice())
            .maxPrice(record.maxPrice())
            .modalPrice(record.modalPrice())
            .build();
    }

    public List<MandiPrice> getAllMandiPrices() {
        return repository.findAll();
    }

    public List<String> findDistinctStates() {
        return repository.findDistinctStates();
    }

    public List<String> findDistinctDistrictsByState(String state) {
        return repository.findDistinctDistrictsByState(state);
    }

    public List<String> findDistinctCommoditiesByStateAndDistrict(String state,String district) {
        return repository.findDistinctCommoditiesByStateAndDistrict(state, district);
    }

    public List<MandiPrice> searchPrices(String state, String district, String commodity) {
    return repository.findByStateAndDistrictAndCommodityOrderByArrivalDateDesc(state, district, commodity);
    }
}