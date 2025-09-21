package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.Connection;
import com.mycompany.travelmanagementsystem.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Connection> sendRequest(@RequestBody Map<String, Long> payload) {
        Long requesterId = payload.get("requesterId");
        Long receiverId = payload.get("receiverId");
        Connection newConnection = connectionService.sendConnectionRequest(requesterId, receiverId);
        // THIS LINE WAS MISSING FROM THE PREVIOUS VERSION
        return ResponseEntity.ok(newConnection);
    }

    @GetMapping("/pending/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Connection>> getPendingRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionService.getPendingRequests(userId));
    }
    
    @GetMapping("/accepted/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Connection>> getAcceptedConnections(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionService.getAcceptedConnections(userId));
    }

    @PutMapping("/{connectionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Connection> updateConnectionStatus(@PathVariable Long connectionId, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        return ResponseEntity.ok(connectionService.updateConnectionStatus(connectionId, status));
    }
}

