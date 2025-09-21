package com.mycompany.travelmanagementsystem.controller;

import com.mycompany.travelmanagementsystem.entity.User;
import com.mycompany.travelmanagementsystem.service.MyUserDetailsService;
import com.mycompany.travelmanagementsystem.service.UserService;
import com.mycompany.travelmanagementsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

// A helper class for the JWT response
class AuthenticationResponse {
    private final String jwt;
    public AuthenticationResponse(String jwt) { this.jwt = jwt; }
    public String getJwt() { return jwt; }
}

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User loginDetails) throws Exception {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Optional<User> userOptional = userService.findByUsername(principal.getName());
        return userOptional
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/get-security-question")
    public ResponseEntity<?> getSecurityQuestion(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(Map.of("securityQuestion", userOptional.get().getSecurityQuestion()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PostMapping("/validate-answer")
    public ResponseEntity<?> validateAnswer(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String answer = request.get("answer");
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent() && userService.validateSecurityAnswer(userOptional.get(), answer)) {
            return ResponseEntity.ok("Answer is correct");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect answer");
    }

    @PostMapping("/reset-password-sq")
    public ResponseEntity<?> resetPasswordWithSecurityQuestion(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("newPassword");
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            userService.resetPassword(userOptional.get(), newPassword);
            return ResponseEntity.ok("Password reset successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
}
