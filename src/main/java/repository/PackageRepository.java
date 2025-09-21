package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Import List

@Repository
public interface PackageRepository extends JpaRepository<TravelPackage, Long> {
    // ADD THIS NEW SEARCH METHOD
    List<TravelPackage> findByPackageNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}