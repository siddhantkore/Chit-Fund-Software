package com.nival.chit.controllers.chitgroup;

import com.nival.chit.dto.ChitGroupDTO;
import com.nival.chit.dto.CreateChitGroupDTO;
import com.nival.chit.enums.ChitGroupStatus;
import com.nival.chit.services.ChitGroupService;
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
 * REST controller for chit group lifecycle operations.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Creating and deleting chit groups</li>
 *     <li>Updating group metadata (name, duration, monthly amount, etc.)</li>
 *     <li>Searching chit groups by ID, code, or name</li>
 *     <li>Managing group status (active, pending, completed, inactive)</li>
 * </ul>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chit/chit/")
@Tag(name = "Chit Groups", description = "Create, manage, and search chit groups.")
public class ChitGroupController {
    private final ChitGroupService chitGroupService;

    /**
     * Create a new chit group.
     * Automatically creates an associated fund for the group.
     * 
     * @param createDTO the chit group creation data
     * @return the created chit group DTO
     */
    @PostMapping
    @Operation(
        summary = "Create a new chit group",
        description = "Creates a new chit group with specified parameters. " +
                     "Automatically generates a unique group code and creates an associated fund. " +
                     "The user specified by adminId will be auto-added as the group admin."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Chit group created successfully",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ChitGroupDTO> createChitGroup(
            @RequestBody CreateChitGroupDTO createDTO) {
        ChitGroupDTO group = chitGroupService.createChitGroup(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    /**
     * Get chit group by ID.
     * 
     * @param groupId the chit group ID
     * @return chit group DTO
     */
    @GetMapping("/{groupId}")
    @Operation(
        summary = "Get chit group by ID",
        description = "Retrieves chit group information by group ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chit group found",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<ChitGroupDTO> getChitGroupById(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        ChitGroupDTO group = chitGroupService.getChitGroupById(groupId);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    /**
     * Get chit group by group code.
     * 
     * @param groupCode the unique group code
     * @return chit group DTO
     */
    @GetMapping("/code/{groupCode}")
    @Operation(
        summary = "Get chit group by code",
        description = "Retrieves chit group information by unique group code."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chit group found",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<ChitGroupDTO> getChitGroupByCode(
            @Parameter(description = "Unique group code", required = true)
            @PathVariable String groupCode) {
        ChitGroupDTO group = chitGroupService.getChitGroupByCode(groupCode);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    /**
     * Search chit groups by name.
     * Performs case-insensitive partial matching.
     * 
     * @param name the name to search
     * @return list of matching chit group DTOs
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search chit groups by name",
        description = "Searches for chit groups by name using case-insensitive partial matching."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search completed",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        )
    })
    public ResponseEntity<List<ChitGroupDTO>> searchChitGroupsByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name) {
        List<ChitGroupDTO> groups = chitGroupService.searchChitGroupsByName(name);
        return ResponseEntity.ok(groups);
    }

    /**
     * Get all chit groups.
     * 
     * @return list of all chit group DTOs
     */
    @GetMapping("/all")
    @Operation(
        summary = "Get all chit groups",
        description = "Retrieves a list of all chit groups in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved chit groups",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        )
    })
    public ResponseEntity<List<ChitGroupDTO>> getAllChitGroups() {
        List<ChitGroupDTO> groups = chitGroupService.getAllChitGroups();
        return ResponseEntity.ok(groups);
    }

    /**
     * Get chit groups by status.
     * 
     * @param status the group status
     * @return list of chit group DTOs with the specified status
     */
    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get chit groups by status",
        description = "Retrieves all chit groups with a specific status (ACTIVE, PENDING, COMPLETED, INACTIVE)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved chit groups",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        )
    })
    public ResponseEntity<List<ChitGroupDTO>> getChitGroupsByStatus(
            @Parameter(description = "Status of the chit groups", required = true)
            @PathVariable ChitGroupStatus status) {
        List<ChitGroupDTO> groups = chitGroupService.getChitGroupsByStatus(status);
        return ResponseEntity.ok(groups);
    }

    /**
     * Update chit group information.
     * 
     * @param groupId the chit group ID
     * @param updateDTO the updated chit group data
     * @return updated chit group DTO
     */
    @PutMapping("/{groupId}")
    @Operation(
        summary = "Update chit group",
        description = "Updates chit group information such as name, duration, monthly amount, and total members."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chit group updated successfully",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<ChitGroupDTO> updateChitGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @RequestBody CreateChitGroupDTO updateDTO) {
        ChitGroupDTO group = chitGroupService.updateChitGroup(groupId, updateDTO);
        return ResponseEntity.ok(group);
    }

    /**
     * Update chit group status.
     * 
     * @param groupId the chit group ID
     * @param status the new status
     * @return updated chit group DTO
     */
    @PutMapping("/{groupId}/status")
    @Operation(
        summary = "Update chit group status",
        description = "Updates the status of a chit group (ACTIVE, PENDING, COMPLETED, INACTIVE)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = ChitGroupDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<ChitGroupDTO> updateChitGroupStatus(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @Parameter(description = "New status", required = true)
            @RequestParam ChitGroupStatus status) {
        ChitGroupDTO group = chitGroupService.updateChitGroupStatus(groupId, status);
        return ResponseEntity.ok(group);
    }

    /**
     * Delete a chit group.
     * This will cascade delete related records. Use with caution.
     * 
     * @param groupId the chit group ID
     * @return success response
     */
    @DeleteMapping("/{groupId}")
    @Operation(
        summary = "Delete chit group",
        description = "Deletes a chit group. This action will cascade delete related records " +
                     "like memberships, loans, payments, and auctions. Use with caution."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chit group deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<Void> deleteChitGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        chitGroupService.deleteChitGroup(groupId);
        return ResponseEntity.ok().build();
    }
}
