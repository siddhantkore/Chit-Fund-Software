package com.nival.chit.dto;

import com.nival.chit.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User entity.
 * Used for returning user information without exposing sensitive data like passwords.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private Long phone;
    private String email;
    private UserRoles role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

