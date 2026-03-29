package com.alertfarm.alertkisan.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record  TotalRecords(
        @JsonProperty("total") Integer totalRecords
) {}
