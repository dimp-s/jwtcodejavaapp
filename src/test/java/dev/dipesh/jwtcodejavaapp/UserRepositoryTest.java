package dev.dipesh.jwtcodejavaapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import  static org.assertj.core.api.Assertions.assertThat;

import dev.dipesh.jwtcodejavaapp.productapi.UserRepository;
import dev.dipesh.jwtcodejavaapp.userapi.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateUser(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "werter";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User("werterboi@gmail.com", encodedPassword);

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
