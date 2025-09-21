package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.service.LoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    @PostMapping("/award-points")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> awardPoints(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Double amount = Double.valueOf(payload.get("amount").toString());
        loyaltyService.addPointsForBooking(userId, amount);
        return ResponseEntity.ok().build();
    }
}