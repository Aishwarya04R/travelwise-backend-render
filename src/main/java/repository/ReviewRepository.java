package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPackageId(Long packageId);
    List<Review> findByHotelId(Long hotelId);
}