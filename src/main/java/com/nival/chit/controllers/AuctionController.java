package com.nival.chit.controllers;

import com.nival.chit.dto.AuctionDTO;
import com.nival.chit.dto.CreateAuctionDTO;
import com.nival.chit.services.AuctionService;
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
 * REST controller for auction management.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Creating auction records</li>
 *     <li>Viewing auction history for groups and users</li>
 *     <li>Deleting auction records</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/auction/")
@RequiredArgsConstructor
@Tag(name = "Auctions", description = "Manage auctions and track winners.")
public class AuctionController {

    private final AuctionService auctionService;

    /**
     * Create a new auction record.
     * 
     * @param createDTO the auction data
     * @return the created auction DTO
     */
    @PostMapping
    @Operation(
        summary = "Create auction",
        description = "Creates a new auction record for a chit group. " +
                     "Records the winning member, winning amount, commission, and auction date."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Auction created successfully",
            content = @Content(schema = @Schema(implementation = AuctionDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input or auction already exists for month"),
        @ApiResponse(responseCode = "404", description = "Chit group or user not found")
    })
    public ResponseEntity<AuctionDTO> createAuction(@RequestBody CreateAuctionDTO createDTO) {
        AuctionDTO auction = auctionService.createAuction(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(auction);
    }

    /**
     * Get auction by ID.
     * 
     * @param auctionId the auction ID
     * @return auction DTO
     */
    @GetMapping("/{auctionId}")
    @Operation(
        summary = "Get auction by ID",
        description = "Retrieves auction information by auction ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Auction found",
            content = @Content(schema = @Schema(implementation = AuctionDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Auction not found")
    })
    public ResponseEntity<AuctionDTO> getAuctionById(
            @Parameter(description = "ID of the auction", required = true)
            @PathVariable Long auctionId) {
        AuctionDTO auction = auctionService.getAuctionById(auctionId);
        if (auction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auction);
    }

    /**
     * Get all auctions for a chit group.
     * 
     * @param groupId the chit group ID
     * @return list of auction DTOs
     */
    @GetMapping("/group/{groupId}")
    @Operation(
        summary = "Get auctions for a group",
        description = "Retrieves all auction records for a specific chit group, " +
                     "ordered by auction date (newest first)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved auctions",
            content = @Content(schema = @Schema(implementation = AuctionDTO.class))
        )
    })
    public ResponseEntity<List<AuctionDTO>> getAuctionsByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<AuctionDTO> auctions = auctionService.getAuctionsByGroup(groupId);
        return ResponseEntity.ok(auctions);
    }

    /**
     * Get all auctions won by a user.
     * 
     * @param userId the user ID
     * @return list of auction DTOs
     */
    @GetMapping("/winner/{userId}")
    @Operation(
        summary = "Get auctions won by user",
        description = "Retrieves all auctions won by a specific user across all groups."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved auctions",
            content = @Content(schema = @Schema(implementation = AuctionDTO.class))
        )
    })
    public ResponseEntity<List<AuctionDTO>> getAuctionsByWinner(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<AuctionDTO> auctions = auctionService.getAuctionsByWinningMember(userId);
        return ResponseEntity.ok(auctions);
    }

    /**
     * Delete an auction.
     * 
     * @param auctionId the auction ID
     * @return success response
     */
    @DeleteMapping("/{auctionId}")
    @Operation(
        summary = "Delete auction",
        description = "Deletes an auction record. Use with caution as this action cannot be undone."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Auction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Auction not found")
    })
    public ResponseEntity<Void> deleteAuction(
            @Parameter(description = "ID of the auction", required = true)
            @PathVariable Long auctionId) {
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok().build();
    }
}
