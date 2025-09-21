package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Optional<Connection> findByRequesterIdAndReceiverId(Long requesterId, Long receiverId);

    // Find all requests sent TO a user that are still pending
    List<Connection> findByReceiverIdAndStatus(Long receiverId, String status);
    
    // Find all accepted connections for a user (where they are either the requester or receiver)
    List<Connection> findByRequesterIdOrReceiverIdAndStatus(Long userId, Long anotherUserId, String status);
}