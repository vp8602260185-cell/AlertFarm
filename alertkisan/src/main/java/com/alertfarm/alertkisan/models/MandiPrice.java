package com.alertfarm.alertkisan.models;

import com.alertfarm.alertkisan.dto.MandiRecord;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mandi_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandiPrice {
    @EmbeddedId
    private MandiPriceId id;
    
    private String variety;
    private Double minPrice;
    private Double maxPrice;
    private Double modalPrice;

    // Helper method to make mapping easier
    public static MandiPriceId createId(MandiRecord r) {
        return new MandiPriceId(r.state(), r.district(), r.market(), r.commodity(), r.arrivalDate());
    }
}
