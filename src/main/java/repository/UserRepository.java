package com.mycompany.travelmanagementsystem.repository;

import com.mycompany.travelmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByResetToken(String token);
    Optional<User> findByEmail(String email); // <-- ADD THIS
}