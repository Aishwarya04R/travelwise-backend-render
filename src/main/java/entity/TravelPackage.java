package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "packages")
@Getter
@Setter
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageName;
    private String description;
    private int durationDays;
    private BigDecimal basePrice;
    @Column(name = "image_url")
    private String imageUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String highlights;
    private String itinerary;
}