package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter
@Setter
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hotelName;
    private String city;
    private BigDecimal costPerPerson;
    private BigDecimal acRoomCost;
    private BigDecimal foodIncludedCost;
    @Column(name = "image_url")
    private String imageUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String highlights;
}