package dev.dipesh.jwtcodejavaapp.productapi;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.dipesh.jwtcodejavaapp.userapi.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
}
