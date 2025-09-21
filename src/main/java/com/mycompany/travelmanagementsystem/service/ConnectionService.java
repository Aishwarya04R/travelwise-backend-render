package com.mycompany.travelmanagementsystem.service;

import com.mycompany.travelmanagementsystem.entity.Connection;
import com.mycompany.travelmanagementsystem.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    public Connection sendConnectionRequest(Long requesterId, Long receiverId) {
        if (requesterId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send a connection request to yourself.");
        }
        Optional<Connection> existing = connectionRepository.findByRequesterIdAndReceiverId(requesterId, receiverId);
        if (existing.isPresent()) {
            throw new IllegalStateException("Connection request already sent.");
        }

        Connection newConnection = new Connection();
        newConnection.setRequesterId(requesterId);
        newConnection.setReceiverId(receiverId);
        newConnection.setStatus("PENDING");
        return connectionRepository.save(newConnection);
    }

    public List<Connection> getPendingRequests(Long userId) {
        return connectionRepository.findByReceiverIdAndStatus(userId, "PENDING");
    }

    public Connection updateConnectionStatus(Long connectionId, String status) {
        Connection connection = connectionRepository.findById(connectionId)
            .orElseThrow(() -> new IllegalArgumentException("Connection not found"));
        
        connection.setStatus(status); // "ACCEPTED" or "DECLINED"
        return connectionRepository.save(connection);
    }
    
    public List<Connection> getAcceptedConnections(Long userId) {
        return connectionRepository.findByRequesterIdOrReceiverIdAndStatus(userId, userId, "ACCEPTED");
    }
}