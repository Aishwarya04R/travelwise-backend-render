package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "package_bookings")
@Getter
@Setter
public class PackageBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long packageId;
    private int persons;
    private BigDecimal totalPrice;
    @Column(updatable = false)
    private LocalDateTime bookingDate;
}