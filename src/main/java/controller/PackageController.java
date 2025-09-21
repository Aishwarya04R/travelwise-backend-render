package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import com.mycompany.travelmanagementsystem.entity.TravelPackage; // Updated import
import com.mycompany.travelmanagementsystem.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public List<TravelPackage> getAllPackages() { // Updated type
        return packageService.getAllPackages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelPackage> getPackageById(@PathVariable Long id) { // Updated type
        Optional<TravelPackage> packageOptional = packageService.getPackageById(id);
        return packageOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/book")
    public PackageBooking bookPackage(@RequestBody PackageBooking booking) {
        return packageService.createBooking(booking);
    }
    
    @GetMapping("/search")
    public List<TravelPackage> searchPackages(@RequestParam String query) {
        return packageService.searchPackages(query);
    }
}
