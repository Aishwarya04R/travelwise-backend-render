package com.mycompany.travelmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "connections")
@Data
@Getter // Add this annotation
@Setter // Add this annotation
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long requesterId;
    
    private Long receiverId;
    
    private String status;
}

