package com.nival.chit.controllers;

import com.nival.chit.dto.LedgerDTO;
import com.nival.chit.services.LedgerService;
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
 * REST controller for ledger and statement endpoints.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Viewing ledger entries for groups and users</li>
 *     <li>Downloading ledger PDFs (implementation pending)</li>
 * </ul>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chit/ledger/")
@Tag(name = "Ledger", description = "Download and inspect group ledgers.")
public class LedgerController {

    private final LedgerService ledgerService;

    /**
     * Get all ledger entries.
     * 
     * @return list of all ledger DTOs
     */
    @GetMapping("/all")
    @Operation(
        summary = "Get all ledger entries",
        description = "Retrieves all ledger entries in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ledger entries",
            content = @Content(schema = @Schema(implementation = LedgerDTO.class))
        )
    })
    public ResponseEntity<List<LedgerDTO>> getAllLedgerRecords() {
        List<LedgerDTO> entries = ledgerService.getAllLedgerRecords();
        return ResponseEntity.ok(entries);
    }

    /**
     * Get ledger entry by ID.
     * 
     * @param ledgerId the ledger entry ID
     * @return ledger DTO
     */
    @GetMapping("/{ledgerId}")
    @Operation(
        summary = "Get ledger entry by ID",
        description = "Retrieves a specific ledger entry by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ledger entry found",
            content = @Content(schema = @Schema(implementation = LedgerDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Ledger entry not found")
    })
    public ResponseEntity<LedgerDTO> getLedgerById(
            @Parameter(description = "ID of the ledger entry", required = true)
            @PathVariable Long ledgerId) {
        LedgerDTO entry = ledgerService.getLedgerById(ledgerId);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entry);
    }

    /**
     * Get all ledger entries for a chit group.
     * 
     * @param groupId the chit group ID
     * @return list of ledger DTOs
     */
    @GetMapping("/group/{groupId}")
    @Operation(
        summary = "Get ledger entries for a group",
        description = "Retrieves all ledger entries for a specific chit group, " +
                     "ordered by entry date (oldest first)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ledger entries",
            content = @Content(schema = @Schema(implementation = LedgerDTO.class))
        )
    })
    public ResponseEntity<List<LedgerDTO>> getLedgerByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<LedgerDTO> entries = ledgerService.getLedgerByGroup(groupId);
        return ResponseEntity.ok(entries);
    }

    /**
     * Get all ledger entries for a user.
     * 
     * @param userId the user ID
     * @return list of ledger DTOs
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get ledger entries for a user",
        description = "Retrieves all ledger entries for a specific user across all groups."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ledger entries",
            content = @Content(schema = @Schema(implementation = LedgerDTO.class))
        )
    })
    public ResponseEntity<List<LedgerDTO>> getLedgerByUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<LedgerDTO> entries = ledgerService.getLedgerByUser(userId);
        return ResponseEntity.ok(entries);
    }

    /**
     * Get all ledger entries for a user in a specific group.
     * 
     * @param userId the user ID
     * @param groupId the chit group ID
     * @return list of ledger DTOs
     */
    @GetMapping("/user/{userId}/group/{groupId}")
    @Operation(
        summary = "Get ledger entries for user in group",
        description = "Retrieves all ledger entries for a specific user in a chit group."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ledger entries",
            content = @Content(schema = @Schema(implementation = LedgerDTO.class))
        )
    })
    public ResponseEntity<List<LedgerDTO>> getLedgerByUserAndGroup(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<LedgerDTO> entries = ledgerService.getLedgerByUserAndGroup(userId, groupId);
        return ResponseEntity.ok(entries);
    }

    /**
     * Download a PDF representation of the ledger.
     * Implementation is pending - requires PDF generation library.
     *
     * @param groupId the chit group ID (optional)
     * @param userId the user ID (optional)
     * @return PDF byte array
     */
    @GetMapping("/pdf")
    @Operation(
        summary = "Download ledger PDF",
        description = "Returns a PDF snapshot of the ledger. " +
                     "Can be filtered by group or user. " +
                     "Implementation pending - requires PDF generation library."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
        @ApiResponse(responseCode = "501", description = "PDF generation not yet implemented")
    })
    public ResponseEntity<byte[]> downloadPdf(
            @Parameter(description = "Optional: ID of the chit group")
            @RequestParam(required = false) Long groupId,
            @Parameter(description = "Optional: ID of the user")
            @RequestParam(required = false) Long userId) {
        // TODO: Implement PDF generation using a library like iText or Apache PDFBox
        return ResponseEntity.status(501).build();
    }
}
