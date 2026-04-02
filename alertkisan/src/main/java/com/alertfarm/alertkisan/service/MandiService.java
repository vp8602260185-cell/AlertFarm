package com.alertfarm.alertkisan.service;

import com.alertfarm.alertkisan.dto.*;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.repository.MandiRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MandiService {

    @Value("${mandi.api.key}")
    private String apiKey;

    @Value("${mandi.api.url}")
    String url;

    @Value("${mandi.api.url_historical}")
    String historicalUrl;

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

    public TotalRecords fetchTotalCount(String state, String district, String commodity) {
        // Use UriComponentsBuilder to handle spaces and special characters safely
        String finalUrl = this.historicalUrl
                        + apiKey 
                        + "&format="+format
                        + "&limit=1"
                        + "&filters[state]="+state
                        + "&filters[district]="+district
                        + "&filters[commodity]="+commodity;

        try {
            TotalRecords response = restTemplate.getForObject(finalUrl, TotalRecords.class);
            if (response != null) {
                System.out.println("Filtered Total Records for " + commodity + ": " + response.totalRecords());
                return response;
            }
        } catch (Exception e) {
            System.err.println("Error fetching filtered count: " + e.getMessage());
        }
        
        return null;
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
    private void fetchBatch(int offset,String state, String district, String commodity) {
        String url = this.historicalUrl
                        + apiKey 
                        + "&format=json"
                        + "&limit=" + limit
                        + "&offset=" + offset
                        + "&filters[state]="+state
                        + "&filters[district]="+district
                        + "&filters[commodity]="+commodity;
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
    @Scheduled(cron = "0 0 10 * * *")
    public void fetchAndSaveMandiData() {
        int total = fetchTotalCount().totalRecords();
        System.out.println("Total Records: " + total);  
        for (int offset = 0; offset < total; offset += limit) {
            System.out.println("Fetching batch with offset: " + offset);
            fetchBatch(offset);
        }
    }
    
    
    public void fetchAndSaveMandiData(String state, String district, String commodity) {
        int total = fetchTotalCount(state,district,commodity).totalRecords();
        System.out.println("Total Records: " + total);  
        for (int offset = 0; offset < total; offset += limit) {
            System.out.println("Fetching batch with offset: " + offset);
            fetchBatch(offset,state,district,commodity);
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

    public List<MandiPrice> getLatestMandiPrices(String state, String district, String commodity) {

        LocalDate latestDate = repository.findLatestArrivalDate(state, district, commodity);

        if (latestDate != null) {
            System.out.println("Found latest data for date: "+latestDate);
            return repository.findBySpecificDate(state, district, commodity, latestDate);
        }
        // log.warn("No data found in DB. Consider triggering an on-demand API fetch.");
        return new ArrayList<>();
    }

    public List<MandiPrice> getRecentHistory(String state, String district, String commodity,Integer days) {
        List<LocalDate> dates=repository.findLastNAvailableDates(state, district, commodity, PageRequest.of(0, days));
        if (dates.isEmpty()) return new ArrayList<>();
        
        List<MandiPrice> allRecords = repository.findHistory(state, district, commodity, dates);

        // 3. Group by Date and keep the MAX Modal Price record
        // Using TreeMap with Collections.reverseOrder() to keep newest dates at the top
        Map<LocalDate, MandiPrice> bestPricesMap = new TreeMap<>(Collections.reverseOrder());

        for (MandiPrice current : allRecords) {
            LocalDate date = current.getId().getArrivalDate();
            if (!bestPricesMap.containsKey(date)) {
                bestPricesMap.put(date, current);
            } else {
                MandiPrice existing = bestPricesMap.get(date);
                if (current.getModalPrice() > existing.getModalPrice()) {
                    bestPricesMap.put(date, current);
                }
            }
        }
        return new ArrayList<>(bestPricesMap.values());
    }
}