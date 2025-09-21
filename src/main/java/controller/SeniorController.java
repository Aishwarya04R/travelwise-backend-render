package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.TravelProfile;
import com.mycompany.travelmanagementsystem.entity.User;
import com.mycompany.travelmanagementsystem.repository.TravelProfileRepository;
import com.mycompany.travelmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/senior-connect")
public class SeniorController {

    @Autowired
    private TravelProfileRepository travelProfileRepository;

    @Autowired
    private UserRepository userRepository;
    
    // A DTO to combine profile and user data
    public static class SeniorMatchDTO {
        public TravelProfile profile;
        public String username;
        public SeniorMatchDTO(TravelProfile profile, String username) {
            this.profile = profile;
            this.username = username;
        }
    }

    @GetMapping("/matches/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SeniorMatchDTO>> findSeniorMatches(@PathVariable Long userId) {
        
        return travelProfileRepository.findByUserId(userId).map(userProfile -> {
            
            // This is our specialized matching algorithm for seniors
            List<SeniorMatchDTO> potentialMatches = travelProfileRepository.findAll().stream()
                .filter(profile -> 
                    // Basic Filters: Must not be the same user, and both must be 55 or older
                    !profile.getUserId().equals(userId) &&
                    profile.getAge() != null && profile.getAge() >= 55 &&
                    userProfile.getAge() != null && userProfile.getAge() >= 55 &&
                    
                    // Compatibility Filters: Must have the same mobility level and travel pace
                    profile.getMobilityLevel() != null &&
                    profile.getMobilityLevel().equalsIgnoreCase(userProfile.getMobilityLevel()) &&
                    profile.getTravelPace() != null &&
                    profile.getTravelPace().equalsIgnoreCase(userProfile.getTravelPace())
                )
                .map(matchProfile -> {
                    String username = userRepository.findById(matchProfile.getUserId())
                                        .map(User::getUsername).orElse("Unknown User");
                    return new SeniorMatchDTO(matchProfile, username);
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(potentialMatches);

        }).orElse(ResponseEntity.notFound().build());
    }
}

