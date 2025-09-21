package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotel_bookings")
@Getter
@Setter
public class HotelBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long hotelId;
    private int persons;
    private int days;
    private boolean acIncluded;
    private boolean foodIncluded;
    private BigDecimal totalPrice;
    @Column(updatable = false)
    private LocalDateTime bookingDate;
}