package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.TravelProfile;
import com.mycompany.travelmanagementsystem.entity.User;
import com.mycompany.travelmanagementsystem.repository.TravelProfileRepository;
import com.mycompany.travelmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private TravelProfileRepository travelProfileRepository;

    @Autowired
    private UserRepository userRepository;

    // A special DTO class to combine profile and user data for the response
    public static class MatchDTO {
        public TravelProfile profile;
        public String username;
        public MatchDTO(TravelProfile profile, String username) {
            this.profile = profile;
            this.username = username;
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TravelProfile> getProfileByUserId(@PathVariable Long userId) {
        return travelProfileRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TravelProfile> createOrUpdateProfile(@RequestBody TravelProfile profile) {
        TravelProfile finalProfile = travelProfileRepository.findByUserId(profile.getUserId())
            .map(existingProfile -> {
                existingProfile.setDestination(profile.getDestination());
                existingProfile.setStartDate(profile.getStartDate());
                existingProfile.setEndDate(profile.getEndDate());
                existingProfile.setAge(profile.getAge());
                existingProfile.setGender(profile.getGender());
                existingProfile.setTravelPace(profile.getTravelPace());
                existingProfile.setBudget(profile.getBudget());
                existingProfile.setInterests(profile.getInterests());
                existingProfile.setAboutMe(profile.getAboutMe());
                return existingProfile;
            }).orElse(profile);

        TravelProfile savedProfile = travelProfileRepository.save(finalProfile);
        return ResponseEntity.ok(savedProfile);
    }

    @GetMapping("/matches/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MatchDTO>> findMatches(@PathVariable Long userId) {
        System.out.println("--- Finding matches for user ID: " + userId);
        return travelProfileRepository.findByUserId(userId).map(userProfile -> {
            System.out.println("--- User's profile found. Destination: " + userProfile.getDestination());
            List<MatchDTO> potentialMatches = travelProfileRepository.findAll().stream()
                .filter(profile -> {
                    boolean isMatch = !profile.getUserId().equals(userId) &&
                                      profile.getDestination() != null &&
                                      profile.getDestination().equalsIgnoreCase(userProfile.getDestination()) &&
                                      datesOverlap(userProfile, profile);
                    System.out.println("--- Checking profile for user ID: " + profile.getUserId() + ". Is it a match? " + isMatch);
                    return isMatch;
                })
                .map(matchProfile -> {
                    String username = userRepository.findById(matchProfile.getUserId())
                                        .map(User::getUsername).orElse("Unknown User");
                    return new MatchDTO(matchProfile, username);
                })
                .collect(Collectors.toList());
            System.out.println("--- Found " + potentialMatches.size() + " matches.");
            return ResponseEntity.ok(potentialMatches);
        }).orElse(ResponseEntity.notFound().build());
    }

    private boolean datesOverlap(TravelProfile p1, TravelProfile p2) {
        if (p1.getStartDate() == null || p1.getEndDate() == null || p2.getStartDate() == null || p2.getEndDate() == null) {
            return false;
        }
        return !p1.getStartDate().isAfter(p2.getEndDate()) && !p2.getStartDate().isAfter(p1.getEndDate());
    }
}

