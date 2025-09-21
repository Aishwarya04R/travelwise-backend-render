package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.TravelPackage;
import com.mycompany.travelmanagementsystem.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.mycompany.travelmanagementsystem.entity.Hotel; // Import Hotel
import com.mycompany.travelmanagementsystem.service.HotelService; // Import HotelService
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PackageService packageService;
    
    @Autowired // <-- INJECT THE HOTEL SERVICE
    private HotelService hotelService;

    @PostMapping("/packages")
    @PreAuthorize("hasRole('ADMIN')") // This secures the endpoint
    public ResponseEntity<TravelPackage> addPackage(@RequestBody TravelPackage newPackage) {
        TravelPackage savedPackage = packageService.savePackage(newPackage);
        return ResponseEntity.ok(savedPackage);
    }
    
    @PostMapping("/hotels")
    @PreAuthorize("hasRole('ADMIN')") // This secures the endpoint
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel newHotel) {
        Hotel savedHotel = hotelService.saveHotel(newHotel);
        return ResponseEntity.ok(savedHotel);
    }
    
    @PutMapping("/packages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TravelPackage> updatePackage(@PathVariable Long id, @RequestBody TravelPackage packageDetails) {
        return ResponseEntity.ok(packageService.updatePackage(id, packageDetails));
    }

    @DeleteMapping("/packages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.ok().build();
    }

    // --- Hotel Management ---
    @PutMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetails) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotelDetails));
    }

    @DeleteMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok().build();
    }
    
}