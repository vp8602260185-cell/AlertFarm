package com.alertfarm.alertkisan.service;

import com.alertfarm.alertkisan.repository.MetadataRepository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alertfarm.alertkisan.dto.MandiApiResponse;
import com.alertfarm.alertkisan.dto.MandiRecord;
import com.alertfarm.alertkisan.dto.TotalRecords;
import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.models.Metadata;
import com.alertfarm.alertkisan.repository.MandiRepository;

import org.springframework.web.client.RestTemplate;

@Service
public class FetchHistoricService {
    private final MetadataRepository metadataRepository;

    @Value("${mandi.api.key}")
    private String apiKey;

    @Value("${mandi.api.url_historical}")
    String historicalUrl;

    @Value("${mandi.api.format}")
    String format;

    @Value("${mandi.api.limit}")
    int limit;


    private  RestTemplate restTemplate;

    @Autowired
    private MandiRepository mandiRepository;

    @Autowired
    private MandiService mandiService;

    FetchHistoricService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        this.restTemplate = new RestTemplate();
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

    private List<MandiPrice> fetchBatch(int offset,String state, String district, String commodity) {
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
        return entities;
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
        
    public List<MandiPrice>fetchAndSaveMandiData(String state, String district, String commodity) {
        int total = fetchTotalCount(state,district,commodity).totalRecords();
        System.out.println("Total Records: " + total);  
        List<MandiPrice> allEntities = new ArrayList<>();
        for (int offset = 0; offset < total; offset += limit) {
            System.out.println("Fetching batch with offset: " + offset);
            allEntities.addAll(fetchBatch(offset,state,district,commodity));
        }
        return allEntities;
    }


    public int countTotalRecords(String state, String district, String commodity) {
        TotalRecords data=fetchTotalCount(state, district, commodity);
        int total_records=data.totalRecords();
        System.out.println("Total Records for " + commodity + " in " + district + ", " + state + ": " + total_records);
        return total_records;
    }

    public int storeHistoricRecordForAll(){
        int total_records=0;
        List<String> states=mandiService.findDistinctStates();
        for(String state: states){
            List<String> districts=mandiService.findDistinctDistrictsByState(state);
            for(String district: districts){
                List<String> commodities=mandiService.findDistinctCommoditiesByStateAndDistrict(state, district);
                for(String commodity: commodities){
                    
                    Boolean isPresent=metadataRepository.findByStateDistrictCommodity(state, district, commodity);
                    if(isPresent){
                        System.out.println("Data already fetched for " + commodity + " in " + district + ", " + state);
                        continue;
                    }

                    TotalRecords data=fetchTotalCount(state, district, commodity);
                    System.out.println("Total Records for " + commodity + " in " + district + ", " + state + ": " + data.totalRecords());
                    if(data.totalRecords()==0){
                        System.out.println("No records found for " + commodity + " in " + district + ", " + state);
                        continue;
                    }
                    List<MandiPrice> mandi_price=fetchAndSaveMandiData(state, district, commodity);
                    processMandiData(state, district, commodity, data, mandi_price);

                    total_records+=data.totalRecords();
                    
                }
            }
        }
        System.out.println("Total Records in the Database: " + total_records);
        return total_records;
    }
    
    @Transactional // This ensures the entire method is one unit of work
    public void processMandiData(String state, String district, String commodity, TotalRecords data,List<MandiPrice> mandi_price) {
        try {
            // 1. Create and Save Metadata
            Metadata entity = Metadata.builder()
                    .fetched(true)
                    .fetchedAt(java.time.LocalDateTime.now())
                    .total_records(data.totalRecords())
                    .state(state)
                    .district(district)
                    .commodity(commodity)
                    .build();
            
            metadataRepository.save(entity);

            // 2. Fetch and Save the actual data
            // If this method fails, the Metadata record above will be DELETED (rolled back)
            mandiRepository.saveAll(mandi_price);
            System.out.println("Sleep for 5 seconds to simulate delay and test rollback...");
            Thread.sleep(5000);  
        } catch (Exception e) {
            System.out.println("Failed to sync data for {}. Rolling back transaction."+commodity+e);
            // We re-throw the exception to trigger the rollback
            throw new RuntimeException(e);
        }
    }
}
