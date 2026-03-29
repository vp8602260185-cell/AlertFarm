package com.alertfarm.alertkisan.repository;

import java.util.List;

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
} 
