package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.Hotel;
import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import com.mycompany.travelmanagementsystem.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
    
    // New endpoint to handle search requests
    @GetMapping("/search")
    public List<Hotel> searchHotels(@RequestParam String query) {
        return hotelService.searchHotels(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        return hotel.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/book")
    public HotelBooking bookHotel(@RequestBody HotelBooking booking) {
        return hotelService.createHotelBooking(booking);
    }
}
