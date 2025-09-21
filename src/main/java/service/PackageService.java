package com.mycompany.travelmanagementsystem.service;

import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import com.mycompany.travelmanagementsystem.entity.TravelPackage;
import com.mycompany.travelmanagementsystem.repository.PackageBookingRepository;
import com.mycompany.travelmanagementsystem.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private PackageBookingRepository packageBookingRepository;

    public List<TravelPackage> getAllPackages() { return packageRepository.findAll(); }
    public Optional<TravelPackage> getPackageById(Long id) { return packageRepository.findById(id); }
    public PackageBooking createBooking(PackageBooking booking) { return packageBookingRepository.save(booking); }
    public List<PackageBooking> getBookingsByUserId(Long userId) { return packageBookingRepository.findByUserId(userId); }
    public TravelPackage savePackage(TravelPackage travelPackage) { return packageRepository.save(travelPackage); }
    public void deletePackage(Long id) { packageRepository.deleteById(id); }

    public TravelPackage updatePackage(Long id, TravelPackage packageDetails) {
        TravelPackage existingPackage = packageRepository.findById(id).orElseThrow(() -> new RuntimeException("Package not found"));
        existingPackage.setPackageName(packageDetails.getPackageName());
        existingPackage.setDescription(packageDetails.getDescription());
        existingPackage.setDurationDays(packageDetails.getDurationDays());
        existingPackage.setBasePrice(packageDetails.getBasePrice());
        existingPackage.setImageUrl(packageDetails.getImageUrl());
        return packageRepository.save(existingPackage);
    }
    
    public List<TravelPackage> searchPackages(String query) {
        return packageRepository.findByPackageNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }
}