package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "travel_profiles")
@Getter
@Setter
public class TravelProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    // General fields
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer age;
    private String gender;
    private String travelPace;
    private String budget;
    private String interests;
    private String aboutMe;

    // Senior-specific fields
    private String mobilityLevel;
    private String healthNotes;
}