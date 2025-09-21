package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // This new method will search for hotels by name or city, ignoring case
    List<Hotel> findByHotelNameContainingIgnoreCaseOrCityContainingIgnoreCase(String hotelName, String city);
}
