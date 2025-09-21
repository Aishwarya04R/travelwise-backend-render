package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.PackageBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageBookingRepository extends JpaRepository<PackageBooking, Long> {
    List<PackageBooking> findByUserId(Long userId);
}