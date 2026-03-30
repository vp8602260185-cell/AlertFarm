package com.alertfarm.alertkisan.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alertfarm.alertkisan.models.MandiPrice;
import com.alertfarm.alertkisan.models.MandiPriceId;

public interface MandiRepository  extends JpaRepository<MandiPrice, MandiPriceId> {

    @Query("SELECT DISTINCT m.id.state FROM MandiPrice m ORDER BY m.id.state")
    List<String> findDistinctStates();

    @Query("SELECT DISTINCT m.id.district FROM MandiPrice m WHERE m.id.state = :state ORDER BY m.id.district")
    List<String> findDistinctDistrictsByState(String state);

    @Query("SELECT DISTINCT m.id.commodity FROM MandiPrice m WHERE m.id.state = :state AND m.id.district = :district ORDER BY m.id.commodity")
    List<String> findDistinctCommoditiesByStateAndDistrict(String state, String district);
    
    @Query("SELECT m FROM MandiPrice m WHERE m.id.state = :state AND m.id.district = :district AND m.id.commodity = :commodity ORDER BY m.id.arrivalDate DESC") 
    List<MandiPrice> findByStateAndDistrictAndCommodityOrderByArrivalDateDesc(String state, String district, String commodity);

    // Step 1: Find the latest date present in the DB for this filter
    @Query("SELECT MAX(m.id.arrivalDate) FROM MandiPrice m " +
           "WHERE m.id.state = :state AND m.id.district = :district AND m.id.commodity = :commodity")
    LocalDate findLatestArrivalDate(String state, String district, String commodity);

    // Step 2: Fetch all records for a specific date (already in your repo)
    @Query("SELECT m FROM MandiPrice m WHERE m.id.state = :state " + 
           "AND m.id.district = :district AND m.id.commodity = :commodity " +
           "AND m.id.arrivalDate = :date")
    List<MandiPrice> findBySpecificDate(String state, String district, String commodity, LocalDate date);

    // Optional: If you want to fetch last N days of data
    @Query("SELECT m FROM MandiPrice m WHERE m.id.state = :state AND m.id.district = :district " +
           "AND m.id.commodity = :commodity AND m.id.arrivalDate IN :dates ORDER BY m.id.arrivalDate DESC")
    List<MandiPrice> findHistory(String state, 
                                 String district, 
                                 String commodity, 
                                 List<LocalDate> dates);
    
    @Query("SELECT DISTINCT m.id.arrivalDate FROM MandiPrice m WHERE m.id.state = :state AND m.id.district = :district " +
           "AND m.id.commodity = :commodity ORDER BY m.id.arrivalDate DESC")
    List<LocalDate> findLastNAvailableDates(String state, 
                                            String district, 
                                            String commodity,
                                            Pageable pageable);                           

} 
