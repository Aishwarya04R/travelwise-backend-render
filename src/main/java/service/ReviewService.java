package com.mycompany.travelmanagementsystem.service;

import com.mycompany.travelmanagementsystem.entity.Review;
import com.mycompany.travelmanagementsystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsForPackage(Long packageId) {
        return reviewRepository.findByPackageId(packageId);
    }

    public List<Review> getReviewsForHotel(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    public Review addReview(Review review) {
        // In a real app, you'd verify the user has actually booked the item
        return reviewRepository.save(review);
    }
}