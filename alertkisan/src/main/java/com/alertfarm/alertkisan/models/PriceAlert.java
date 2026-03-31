package com.alertfarm.alertkisan.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "price_alerts")
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlert implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "District is required")
    private String district;
    @NotBlank(message = "Commodity is required")
    private String commodity;
    private Double targetPrice;
    private String alertType;
    private Boolean active;
    private String createdAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;
}
