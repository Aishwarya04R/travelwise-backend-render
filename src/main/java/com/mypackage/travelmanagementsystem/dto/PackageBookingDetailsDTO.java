package com.mycompany.travelmanagementsystem.dto;

import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import com.mycompany.travelmanagementsystem.entity.TravelPackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageBookingDetailsDTO {
    private PackageBooking booking;
    private TravelPackage packageDetails;
}