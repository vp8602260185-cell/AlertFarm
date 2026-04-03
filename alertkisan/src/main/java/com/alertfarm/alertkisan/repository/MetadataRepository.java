package com.alertfarm.alertkisan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.alertfarm.alertkisan.models.Metadata;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata,Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Metadata m " +
           "WHERE m.state = :state AND m.district = :district AND m.commodity = :commodity")
    Boolean findByStateDistrictCommodity(String state, String district, String commodity);
}
