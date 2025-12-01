package com.nival.chit.controllers;

import com.nival.chit.dto.FundsDTO;
import com.nival.chit.services.FundsService;
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
 * REST controller for fund management.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Viewing fund balances and details</li>
 *     <li>Updating fund amounts and interest rates</li>
 *     <li>Administrative views of fund operations</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/funds/")
@RequiredArgsConstructor
@Tag(name = "Funds", description = "View overall funds managed by chit groups.")
public class FundsController {

    private final FundsService fundsService;

    /**
     * Get fund by chit group ID.
     * 
     * @param groupId the chit group ID
     * @return fund DTO
     */
    @GetMapping("/group/{groupId}")
    @Operation(
        summary = "Get fund by group",
        description = "Retrieves fund information for a specific chit group."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fund found",
            content = @Content(schema = @Schema(implementation = FundsDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Fund not found")
    })
    public ResponseEntity<FundsDTO> getFundByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        FundsDTO fund = fundsService.getFundByGroupId(groupId);
        if (fund == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fund);
    }

    /**
     * Get fund by ID.
     * 
     * @param fundId the fund ID
     * @return fund DTO
     */
    @GetMapping("/{fundId}")
    @Operation(
        summary = "Get fund by ID",
        description = "Retrieves fund information by fund ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fund found",
            content = @Content(schema = @Schema(implementation = FundsDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Fund not found")
    })
    public ResponseEntity<FundsDTO> getFundById(
            @Parameter(description = "ID of the fund", required = true)
            @PathVariable Long fundId) {
        FundsDTO fund = fundsService.getFundById(fundId);
        if (fund == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fund);
    }

    /**
     * Get all funds.
     * 
     * @return list of all fund DTOs
     */
    @GetMapping("/all")
    @Operation(
        summary = "Get all funds",
        description = "Retrieves a list of all funds in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved funds",
            content = @Content(schema = @Schema(implementation = FundsDTO.class))
        )
    })
    public ResponseEntity<List<FundsDTO>> getAllFunds() {
        List<FundsDTO> funds = fundsService.getAllFunds();
        return ResponseEntity.ok(funds);
    }

    /**
     * Update fund total amount.
     * 
     * @param fundId the fund ID
     * @param amount the new total amount
     * @return updated fund DTO
     */
    @PutMapping("/{fundId}/amount")
    @Operation(
        summary = "Update fund amount",
        description = "Updates the total amount of a fund."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Amount updated successfully",
            content = @Content(schema = @Schema(implementation = FundsDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Fund not found")
    })
    public ResponseEntity<FundsDTO> updateFundAmount(
            @Parameter(description = "ID of the fund", required = true)
            @PathVariable Long fundId,
            @Parameter(description = "New total amount", required = true)
            @RequestParam Double amount) {
        FundsDTO fund = fundsService.updateFundAmount(fundId, amount);
        return ResponseEntity.ok(fund);
    }

    /**
     * Update fund interest rate.
     * 
     * @param fundId the fund ID
     * @param interestRate the new interest rate
     * @return updated fund DTO
     */
    @PutMapping("/{fundId}/interest-rate")
    @Operation(
        summary = "Update fund interest rate",
        description = "Updates the interest rate of a fund."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Interest rate updated successfully",
            content = @Content(schema = @Schema(implementation = FundsDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Fund not found")
    })
    public ResponseEntity<FundsDTO> updateFundInterestRate(
            @Parameter(description = "ID of the fund", required = true)
            @PathVariable Long fundId,
            @Parameter(description = "New interest rate", required = true)
            @RequestParam Double interestRate) {
        FundsDTO fund = fundsService.updateFundInterestRate(fundId, interestRate);
        return ResponseEntity.ok(fund);
    }
}
