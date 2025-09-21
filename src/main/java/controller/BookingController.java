package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.dto.BookingResponse;
import com.mycompany.travelmanagementsystem.dto.HotelBookingDetailsDTO;
import com.mycompany.travelmanagementsystem.dto.PackageBookingDetailsDTO;
import com.mycompany.travelmanagementsystem.entity.Hotel; // Import Hotel
import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import com.mycompany.travelmanagementsystem.entity.TravelPackage; // Import TravelPackage
import com.mycompany.travelmanagementsystem.service.HotelService;
import com.mycompany.travelmanagementsystem.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/my-bookings/{userId}")
    public ResponseEntity<BookingResponse> getMyBookings(@PathVariable Long userId) {
        // Fetch package bookings and their details
        List<PackageBooking> userPackageBookings = packageService.getBookingsByUserId(userId);
        List<PackageBookingDetailsDTO> packageBookingDetails = userPackageBookings.stream()
            .map(booking -> {
                TravelPackage packageInfo = packageService.getPackageById(booking.getPackageId()).orElse(null); // Updated type
                return new PackageBookingDetailsDTO(booking, packageInfo);
            })
            .collect(Collectors.toList());

        // Fetch hotel bookings and their details
        List<HotelBooking> userHotelBookings = hotelService.getBookingsByUserId(userId);
        List<HotelBookingDetailsDTO> hotelBookingDetails = userHotelBookings.stream()
            .map(booking -> {
                Hotel hotelInfo = hotelService.getHotelById(booking.getHotelId()).orElse(null);
                return new HotelBookingDetailsDTO(booking, hotelInfo);
            })
            .collect(Collectors.toList());
        
        BookingResponse response = new BookingResponse(packageBookingDetails, hotelBookingDetails);
        return ResponseEntity.ok(response);
    }
}
