package com.nival.chit.controllers;

import com.nival.chit.dto.ChangePasswordDTO;
import com.nival.chit.dto.CreateUserDTO;
import com.nival.chit.dto.UpdateUserDTO;
import com.nival.chit.dto.UserDTO;
import com.nival.chit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>User registration and account creation</li>
 *     <li>Viewing and updating user profiles</li>
 *     <li>Changing passwords</li>
 *     <li>User listing and search (for admins)</li>
 * </ul>
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/chit/user/")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Manage users, profiles, and roles in the chit platform.")
public class UserController {

    private final UserService userService;

    /**
     * Register a new user account.
     * Public endpoint - no authentication required.
     * 
     * @param createDTO the user registration data
     * @return the created user DTO
     */
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account. Username and email must be unique. " +
                     "Password will be encrypted before storage."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists")
    })
    public ResponseEntity<UserDTO> registerUser(@RequestBody CreateUserDTO createDTO) {
        UserDTO user = userService.createUser(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Get user by ID.
     * 
     * @param userId the user ID
     * @return user DTO
     */
    @GetMapping("/{userId}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves user information by user ID. " +
                     "Returns user details without sensitive information like password."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username.
     * 
     * @param username the username
     * @return user DTO
     */
    @GetMapping("/username/{username}")
    @Operation(
        summary = "Get user by username",
        description = "Retrieves user information by username."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username of the user", required = true)
            @PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users.
     * Typically used by admins to view all registered users.
     * 
     * @return list of all user DTOs
     */
    @GetMapping("/all")
    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all registered users. " +
                     "Typically used by administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved users",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        )
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user profile information.
     * Users can update their own profile (name, phone, email).
     * 
     * @param userId the user ID
     * @param updateDTO the updated user data
     * @return updated user DTO
     */
    @PutMapping("/{userId}")
    @Operation(
        summary = "Update user profile",
        description = "Updates user profile information such as name, phone, and email. " +
                     "Email must be unique if changed."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updateDTO) {
        UserDTO user = userService.updateUser(userId, updateDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * Change user password.
     * Requires current password for verification.
     * 
     * @param userId the user ID
     * @param changePasswordDTO the password change data
     * @return success response
     */
    @PutMapping("/{userId}/change-password")
    @Operation(
        summary = "Change user password",
        description = "Changes the user's password. Requires current password for verification."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Current password is incorrect"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(userId, changePasswordDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a user account.
     * Typically used by admins. This will cascade delete related records.
     * 
     * @param userId the user ID
     * @return success response
     */
    @DeleteMapping("/{userId}")
    @Operation(
        summary = "Delete user account",
        description = "Deletes a user account. This action will cascade delete related records " +
                     "like memberships, loans, and payments. Use with caution."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
