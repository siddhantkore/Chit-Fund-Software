package com.nival.chit.controllers;

import com.nival.chit.dto.MembershipDTO;
import com.nival.chit.services.ChitGroupMemberOperationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for membership-related operations.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Viewing all groups a user is a member of</li>
 *     <li>Viewing membership details</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/membership/")
@RequiredArgsConstructor
@Tag(name = "Memberships", description = "View user memberships across chit groups.")
public class MembershipController {

    private final ChitGroupMemberOperationsService chitGroupMemberOperationsService;

    /**
     * Get all chit groups a user is a member of.
     * 
     * @param userId the user ID
     * @return list of membership DTOs
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get all groups for a user",
        description = "Retrieves all chit groups that a specific user is a member of. " +
                     "Returns membership details including join date and status."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved memberships",
            content = @Content(schema = @Schema(implementation = MembershipDTO.class))
        )
    })
    public ResponseEntity<List<MembershipDTO>> getUserMemberships(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<MembershipDTO> memberships = chitGroupMemberOperationsService.getUserMemberships(userId);
        return ResponseEntity.ok(memberships);
    }
}
