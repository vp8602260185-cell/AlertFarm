package com.alertfarm.alertkisan.models;

import java.time.LocalDateTime;

import com.alertfarm.alertkisan.dto.MandiRecord;

import jakarta.annotation.Generated;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity()
@Table(name = "metadata")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String state;
    private String district;
    private String commodity;
    private Boolean fetched;
    private LocalDateTime fetchedAt;
    private int total_records;
}
