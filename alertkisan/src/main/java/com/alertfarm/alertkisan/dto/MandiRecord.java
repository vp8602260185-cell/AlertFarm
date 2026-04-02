package com.alertfarm.alertkisan.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MandiRecord(
    @JsonAlias({"state", "State"})
    String state,
    @JsonAlias({"district", "District"})
    String district,
    @JsonAlias({"market", "Market"})
    String market,
    @JsonAlias({"commodity", "Commodity"})
    String commodity,
    @JsonAlias({"variety", "Variety"})
    String variety,
    @JsonAlias({"min_price", "Min_Price"})
    @JsonProperty("min_price") Double minPrice,
    @JsonAlias({"max_price", "Max_Price"})
    @JsonProperty("max_price") Double maxPrice,
    @JsonAlias({"modal_price", "Modal_Price"})
    @JsonProperty("modal_price") Double modalPrice,
    @JsonAlias({"arrival_date", "Arrival_Date"})
    @JsonProperty("arrival_date") String arrivalDate
) {}