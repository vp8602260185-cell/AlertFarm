package com.alertfarm.alertkisan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alertfarm.alertkisan.models.MandiPrice;

public interface MandiRepository  extends JpaRepository<MandiPrice, Long> {

    @Query("SELECT DISTINCT m.state FROM MandiPrice m ORDER BY m.state")
    List<String> findDistinctStates();

    @Query("SELECT DISTINCT m.district FROM MandiPrice m WHERE m.state = :state ORDER BY m.district")
    List<String> findDistinctDistrictsByState(String state);

    @Query("SELECT DISTINCT m.commodity FROM MandiPrice m WHERE m.state = :state AND m.district = :district ORDER BY m.commodity")
    List<String> findDistinctCommoditiesByStateAndDistrict(String state, String district);
    
    @Query("SELECT m FROM MandiPrice m WHERE m.state = :state AND m.district = :district AND m.commodity = :commodity ORDER BY m.arrivalDate DESC") 
    List<MandiPrice> findByStateAndDistrictAndCommodityOrderByArrivalDateDesc(String state, String district, String commodity);
} 
