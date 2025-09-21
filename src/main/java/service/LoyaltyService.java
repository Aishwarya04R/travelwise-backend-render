package com.mycompany.travelmanagementsystem.service;

import com.mycompany.travelmanagementsystem.entity.User;
import com.mycompany.travelmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {

    @Autowired
    private UserRepository userRepository;

    // Award points for a booking
    public void addPointsForBooking(Long userId, Double amountSpent) {
        userRepository.findById(userId).ifPresent(user -> {
            // Award 1 point for every 100 rupees spent
            int pointsToAdd = (int) (amountSpent / 100);
            user.setLoyaltyPoints(user.getLoyaltyPoints() + pointsToAdd);
            userRepository.save(user);
        });
    }
}