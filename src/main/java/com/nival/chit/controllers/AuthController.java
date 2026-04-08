package com.nival.chit.controllers;

import com.nival.chit.dto.LoginRequestDTO;
import com.nival.chit.dto.UserDTO;
import com.nival.chit.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication utilities.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/chit/auth/")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and session utility endpoints.")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Validates the supplied credentials and returns the authenticated user profile.
     *
     * @param loginRequest the username/password payload
     * @return the authenticated user DTO
     */
    @PostMapping("/login")
    @Operation(
        summary = "Validate user credentials",
        description = "Authenticates the supplied username and password and returns the matching user profile."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Credentials accepted",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }

    /**
     * Get the currently authenticated user's profile based on the Basic Auth token.
     * 
     * @return the authenticated user DTO
     */
    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Retrieves the user profile corresponding to the currently authenticated session."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        UserDTO user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
}
