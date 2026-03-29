package com.alertfarm.alertkisan.service;

import com.alertfarm.alertkisan.dto.*;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.repository.MandiRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MandiService {

    @Value("${mandi.api.key}")
    private String apiKey;

    @Value("${mandi.api.url}")
    String url;

    private final MandiRepository repository;
    private final RestTemplate restTemplate;


    public MandiService(MandiRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public TotalRecords fetchTotalCount() {
        String url = this.url
                     + apiKey 
                     + "&format=json"
                     + "&limit=1";

        TotalRecords response = restTemplate.getForObject(url, TotalRecords.class);
        if (response != null) {
            System.out.println("Total Records: " + response.totalRecords());
        } else {
            System.out.println("Failed to fetch total records.");
        }
        return response;
    }

    public void fetchAndSaveMandiData() {
        String url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=" 
                     + apiKey 
                     + "&format=json"
                     + "&limit=10000";
                    //  + "&filters[state]=Madhya Pradesh";

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

    private MandiPrice mapToEntity(MandiRecord record) {
        MandiPrice entity = new MandiPrice();
        entity.setState(record.state());
        entity.setDistrict(record.district());
        entity.setMarket(record.market());
        entity.setCommodity(record.commodity());
        entity.setVariety(record.variety());
        entity.setMinPrice(record.minPrice());
        entity.setMaxPrice(record.maxPrice());
        entity.setModalPrice(record.modalPrice());
        entity.setArrivalDate(record.arrivalDate());
        return entity;
    }

    public List<MandiPrice> getAllMandiPrices() {
        return repository.findAll();
    }
}