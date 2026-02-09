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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for membership-related operations.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Viewing all groups a user is a member of</li>
 *     <li>Viewing membership details</li>
 *     <li>Joining a group by code</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/membership/")
@RequiredArgsConstructor
@Tag(name = "Memberships", description = "View and manage user memberships across chit groups.")
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
                     "Returns membership details including join date, role, and status."
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

    /**
     * Join a group by group code.
     * 
     * @param groupCode the unique group code
     * @param userId the user ID who wants to join
     * @return the created membership DTO
     */
    @PostMapping("/join")
    @Operation(
        summary = "Join a group by code",
        description = "Allows a user to join a chit group by providing its unique group code."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully joined group",
            content = @Content(schema = @Schema(implementation = MembershipDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid code or user already member")
    })
    public ResponseEntity<MembershipDTO> joinByCode(
            @Parameter(description = "The unique group code", required = true)
            @RequestParam String groupCode,
            @Parameter(description = "ID of the user joining", required = true)
            @RequestParam Long userId) {
        MembershipDTO membership = chitGroupMemberOperationsService.joinGroupByCode(groupCode, userId);
        return ResponseEntity.ok(membership);
    }
}
