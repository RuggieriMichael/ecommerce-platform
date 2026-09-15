package com.ecommerce.platform.service;

import com.ecommerce.platform.model.Role;
import com.ecommerce.platform.model.User;
import com.ecommerce.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection: Spring sees this constructor and automatically
    // supplies a UserRepository and PasswordEncoder bean when it creates UserService.
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User(username, email, passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }
}
