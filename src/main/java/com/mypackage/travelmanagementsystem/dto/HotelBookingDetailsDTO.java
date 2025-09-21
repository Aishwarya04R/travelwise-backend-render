package com.mycompany.travelmanagementsystem.dto;

import com.mycompany.travelmanagementsystem.entity.Hotel;
import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelBookingDetailsDTO {
    private HotelBooking booking;
    private Hotel hotelDetails;
}