package com.nival.chit.repository;

import com.nival.chit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides data access operations for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username.
     *
     * @param username the username
     * @return optional user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email the email address
     * @return optional user if found
     */
    Optional<User> findByEmail(String email);
}

