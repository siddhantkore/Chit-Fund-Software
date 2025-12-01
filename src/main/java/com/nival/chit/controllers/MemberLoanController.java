package com.nival.chit.controllers;

import com.nival.chit.dto.MemberLoanResponseDTO;
import com.nival.chit.dto.MemberLoanSummaryDTO;
import com.nival.chit.services.MemberLoanService;
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
 * REST controller for managing member loans.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Group admins to view all loans in their chit group</li>
 *     <li>Members to view their loans across all groups with real-time payable amounts</li>
 *     <li>Retrieving current payable amounts calculated daily</li>
 * </ul>
 * 
 * <p>All loan amounts are calculated in real-time based on daily interest accrual.</p>
 */
@RestController
@RequestMapping("/api/chit/loan/")
@Tag(name = "Loans", description = "View and track member loans across chit groups with real-time daily calculations.")
@RequiredArgsConstructor
public class MemberLoanController {

    private final MemberLoanService memberLoanService;

    /**
     * Get all loans for members in a specific chit group.
     * Primarily used by group admins to monitor all active loans in their group.
     * 
     * @param groupId the ID of the chit group
     * @return list of all member loans in the group with current payable amounts
     */
    @GetMapping("/group/{groupId}")
    @Operation(
        summary = "Get all loans in a chit group",
        description = "Retrieves all member loans for a specific chit group. " +
                     "Used by group admins to view loan status and amounts for all members. " +
                     "Current payable amounts are calculated daily based on interest accrual."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved loans",
            content = @Content(schema = @Schema(implementation = MemberLoanResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<List<MemberLoanResponseDTO>> getAllLoansByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<MemberLoanResponseDTO> loans = memberLoanService.getAllLoansByGroupId(groupId);
        return ResponseEntity.ok(loans);
    }

    /**
     * Get all loans for a specific user across all chit groups.
     * Used by members to view their complete loan portfolio with real-time calculations.
     * 
     * @param userId the ID of the user
     * @return list of loan summaries with current payable amounts per group
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get all loans for a user",
        description = "Retrieves all loans belonging to a specific user across all chit groups. " +
                     "Returns summary information including current payable amounts calculated daily. " +
                     "Useful for members to track their total loan obligations."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved user loans",
            content = @Content(schema = @Schema(implementation = MemberLoanSummaryDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<MemberLoanSummaryDTO>> getAllLoansByUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<MemberLoanSummaryDTO> loans = memberLoanService.getAllLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    /**
     * Get the current payable amount for a user's loan in a specific group.
     * Returns real-time calculation of principle + accumulated interest.
     * 
     * @param userId the ID of the user
     * @param groupId the ID of the chit group
     * @return current payable amount (principle + interest)
     */
    @GetMapping("/user/{userId}/group/{groupId}/payable")
    @Operation(
        summary = "Get current payable amount",
        description = "Calculates and returns the current payable amount (principle + interest) " +
                     "for a user's loan in a specific chit group. " +
                     "Interest is calculated daily from the loan start date."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully calculated current payable"),
        @ApiResponse(responseCode = "404", description = "Loan not found for user in this group")
    })
    public ResponseEntity<Double> getCurrentPayable(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        Double currentPayable = memberLoanService.getCurrentPayable(userId, groupId);
        
        if (currentPayable == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(currentPayable);
    }

    /**
     * Get detailed loan information for a user in a specific group.
     * Returns complete loan details including principle, interest rate, tenure, and current payable.
     * 
     * @param userId the ID of the user
     * @param groupId the ID of the chit group
     * @return detailed loan information with current payable amount
     */
    @GetMapping("/user/{userId}/group/{groupId}")
    @Operation(
        summary = "Get detailed loan information",
        description = "Retrieves detailed loan information for a user in a specific chit group. " +
                     "Includes principle, interest rate, tenure, dates, and current payable amount " +
                     "calculated daily."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved loan details",
            content = @Content(schema = @Schema(implementation = MemberLoanResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Loan not found for user in this group")
    })
    public ResponseEntity<MemberLoanResponseDTO> getLoanByUserAndGroup(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        MemberLoanResponseDTO loan = memberLoanService.getLoanByUserAndGroup(userId, groupId);
        
        if (loan == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(loan);
    }
}
