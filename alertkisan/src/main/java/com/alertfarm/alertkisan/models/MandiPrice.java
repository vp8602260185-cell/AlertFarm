package com.alertfarm.alertkisan.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mandi_prices")
@Data
public class MandiPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;
    private String district;
    private String market;
    private String commodity;
    private String variety;
    private Double minPrice;
    private Double maxPrice;
    private Double modalPrice;
    private String arrivalDate;
}
