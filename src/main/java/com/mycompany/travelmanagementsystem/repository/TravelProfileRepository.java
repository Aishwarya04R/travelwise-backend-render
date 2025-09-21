package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.TravelProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TravelProfileRepository extends JpaRepository<TravelProfile, Long> {
    Optional<TravelProfile> findByUserId(Long userId);
}