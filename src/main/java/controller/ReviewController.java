package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.Review;
import com.mycompany.travelmanagementsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/package/{packageId}")
    public List<Review> getPackageReviews(@PathVariable Long packageId) {
        return reviewService.getReviewsForPackage(packageId);
    }

    @GetMapping("/hotel/{hotelId}")
    public List<Review> getHotelReviews(@PathVariable Long hotelId) {
        return reviewService.getReviewsForHotel(hotelId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Only logged-in users can post reviews
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }
}