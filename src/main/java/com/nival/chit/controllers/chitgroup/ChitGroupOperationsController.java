package com.nival.chit.controllers.chitgroup;

import com.nival.chit.dto.MembershipDTO;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.services.ChitGroupMemberOperationsService;
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
 * REST controller for chit group member operations.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Adding and removing members from chit groups</li>
 *     <li>Viewing group members and user memberships</li>
 *     <li>Managing membership status</li>
 * </ul>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chit/chit/{groupId}/members/")
@Tag(name = "Group Member Operations", description = "Manage members in chit groups.")
public class ChitGroupOperationsController {

    private final ChitGroupMemberOperationsService chitGroupMemberOperationsService;

    /**
     * Add a member to a chit group.
     * 
     * @param groupId the chit group ID
     * @param userId the user ID to add
     * @return the created membership DTO
     */
    @PostMapping("/{userId}")
    @Operation(
        summary = "Add member to chit group",
        description = "Adds a user as a member to a chit group. " +
                     "Checks if group has reached maximum member limit."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Member added successfully",
            content = @Content(schema = @Schema(implementation = MembershipDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "User already a member or group full"),
        @ApiResponse(responseCode = "404", description = "Chit group or user not found")
    })
    public ResponseEntity<MembershipDTO> addMemberToChitGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @Parameter(description = "ID of the user to add", required = true)
            @PathVariable Long userId) {
        MembershipDTO membership = chitGroupMemberOperationsService.addMemberToGroup(groupId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(membership);
    }

    /**
     * Remove a member from a chit group.
     * 
     * @param groupId the chit group ID
     * @param userId the user ID to remove
     * @return success response
     */
    @DeleteMapping("/{userId}")
    @Operation(
        summary = "Remove member from chit group",
        description = "Removes a user from a chit group. " +
                     "This will also remove related records like loans and payments."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Member removed successfully"),
        @ApiResponse(responseCode = "404", description = "Membership not found")
    })
    public ResponseEntity<Void> removeMemberFromChitGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @Parameter(description = "ID of the user to remove", required = true)
            @PathVariable Long userId) {
        chitGroupMemberOperationsService.removeMemberFromGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all members of a chit group.
     * 
     * @param groupId the chit group ID
     * @return list of membership DTOs
     */
    @GetMapping
    @Operation(
        summary = "Get all members of a chit group",
        description = "Retrieves a list of all members in a specific chit group."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved members",
            content = @Content(schema = @Schema(implementation = MembershipDTO.class))
        )
    })
    public ResponseEntity<List<MembershipDTO>> getGroupMembers(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<MembershipDTO> members = chitGroupMemberOperationsService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    /**
     * Update membership status.
     * 
     * @param groupId the chit group ID
     * @param userId the user ID
     * @param status the new status
     * @return updated membership DTO
     */
    @PutMapping("/{userId}/status")
    @Operation(
        summary = "Update membership status",
        description = "Updates the status of a membership (e.g., ACTIVE, INACTIVE)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = MembershipDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Membership not found")
    })
    public ResponseEntity<MembershipDTO> updateMembershipStatus(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "New status", required = true)
            @RequestParam UserStatus status) {
        MembershipDTO membership = chitGroupMemberOperationsService.updateMembershipStatus(groupId, userId, status);
        return ResponseEntity.ok(membership);
    }
}
