package com.mycompany.travelmanagementsystem.dto;

import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private List<PackageBookingDetailsDTO> packageBookings;
    private List<HotelBookingDetailsDTO> hotelBookings;
}