package com.alertfarm.alertkisan.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MandiPriceId implements Serializable {
    
    @Column(length = 50) // Reduced from 255
    private String state;

    @Column(length = 50)
    private String district;

    @Column(length = 100) // Markets can sometimes have longer names
    private String market;

    @Column(length = 100)
    private String commodity;

    @Column(length = 20) // Dates like "29/03/2026" are short
    private LocalDate arrivalDate;
}