package com.alertfarm.alertkisan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MandiRecord(
    String state,
    String district,
    String market,
    String commodity,
    String variety,
    @JsonProperty("min_price") Double minPrice,
    @JsonProperty("max_price") Double maxPrice,
    @JsonProperty("modal_price") Double modalPrice,
    @JsonProperty("arrival_date") String arrivalDate
) {}