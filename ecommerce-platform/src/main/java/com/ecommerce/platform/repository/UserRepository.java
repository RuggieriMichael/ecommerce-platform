package com.ecommerce.platform.repository;

import com.ecommerce.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Extending JpaRepository<User, Long> gives us save(), findById(), findAll(), delete(), etc.
 * for free -- no SQL written. The two methods below are "derived queries": Spring reads the
 * method name and builds the SQL from it automatically.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
