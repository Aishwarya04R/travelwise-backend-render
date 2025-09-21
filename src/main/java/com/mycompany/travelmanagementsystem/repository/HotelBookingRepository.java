package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByUserId(Long userId); // <-- ADD THIS
}