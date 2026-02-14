package com.nival.chit.services;

import com.nival.chit.dto.ChangePasswordDTO;
import com.nival.chit.dto.CreateUserDTO;
import com.nival.chit.dto.UpdateUserDTO;
import com.nival.chit.dto.UserDTO;
import com.nival.chit.entity.User;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 * Handles user registration, profile updates, password changes, and user queries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create a new user account.
     *
     * @param createDTO the user creation data
     * @return the created user DTO
     * @throws IllegalArgumentException if username or email already exists
     */
    @Transactional
    public UserDTO createUser(CreateUserDTO createDTO) {
        try {
            // Check if username already exists
            if (userRepository.findByUsername(createDTO.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists: " + createDTO.getUsername());
            }

            // Check if email already exists
            if (userRepository.findByEmail(createDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + createDTO.getEmail());
            }

            User user = new User();
            user.setName(createDTO.getName());
            user.setUsername(createDTO.getUsername());
            user.setPhone(createDTO.getPhone());
            user.setEmail(createDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
            user.setRole(createDTO.getRole() != null ? createDTO.getRole() : UserRoles.MEMBER);

            user = userRepository.save(user);
            log.info("User created: {}", user.getUsername());

            return convertToDTO(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get user by ID.
     *
     * @param userId the user ID
     * @return user DTO or null if not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Get user by username.
     *
     * @param username the username
     * @return user DTO or null if not found
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Get all users.
     *
     * @return list of all user DTOs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update user profile information.
     *
     * @param userId the user ID
     * @param updateDTO the updated user data
     * @return updated user DTO
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getEmail() != null) {
            // Check if email is already taken by another user
            userRepository.findByEmail(updateDTO.getEmail())
                    .ifPresent(existingUser -> {
                        if (existingUser.getId() != userId) {
                            throw new IllegalArgumentException("Email already exists: " + updateDTO.getEmail());
                        }
                    });
            user.setEmail(updateDTO.getEmail());
        }

        user = userRepository.save(user);
        log.info("User updated: {}", user.getUsername());

        return convertToDTO(user);
    }

    /**
     * Change user password.
     *
     * @param userId the user ID
     * @param changePasswordDTO the password change data
     * @return true if password changed successfully
     * @throws IllegalArgumentException if current password is incorrect or user not found
     */
    @Transactional
    public boolean changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Verify current password
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getUsername());

        return true;
    }

    /**
     * Delete a user account.
     *
     * @param userId the user ID
     * @throws IllegalArgumentException if user not found
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        userRepository.delete(user);
        log.info("User deleted: {}", user.getUsername());
    }

    /**
     * Convert User entity to UserDTO.
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

