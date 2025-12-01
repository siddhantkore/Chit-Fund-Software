package com.nival.chit.controllers;

import com.nival.chit.dto.CreatePaymentDTO;
import com.nival.chit.dto.PaymentDTO;
import com.nival.chit.services.PaymentsService;
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
 * REST controller for payment collection and tracking.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Recording monthly contributions for members</li>
 *     <li>Recording loan repayments</li>
 *     <li>Viewing payment history</li>
 *     <li>Updating payment status</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/payments/")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Record and view contributions and loan repayments.")
public class PaymentsController {

    private final PaymentsService paymentsService;

    /**
     * Record a payment (monthly contribution or loan repayment).
     * 
     * @param createDTO the payment data
     * @return the created payment DTO
     */
    @PostMapping
    @Operation(
        summary = "Record a payment",
        description = "Records a payment for a user in a chit group. " +
                     "Can be used for monthly contributions or loan repayments."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Payment recorded successfully",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User or chit group not found")
    })
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody CreatePaymentDTO createDTO) {
        PaymentDTO payment = paymentsService.createPayment(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    /**
     * Get all payments for a user in a specific group.
     * 
     * @param userId the user ID
     * @param groupId the chit group ID
     * @return list of payment DTOs
     */
    @GetMapping("/user/{userId}/group/{groupId}")
    @Operation(
        summary = "Get payments for user in group",
        description = "Retrieves all payment records for a specific user in a chit group."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved payments",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
        )
    })
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserAndGroup(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<PaymentDTO> payments = paymentsService.getPaymentsByUserAndGroup(userId, groupId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Get all payments for a chit group.
     * 
     * @param groupId the chit group ID
     * @return list of payment DTOs
     */
    @GetMapping("/group/{groupId}")
    @Operation(
        summary = "Get all payments for a group",
        description = "Retrieves all payment records for a chit group. " +
                     "Useful for group admins to view all contributions."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved payments",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
        )
    })
    public ResponseEntity<List<PaymentDTO>> getPaymentsByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<PaymentDTO> payments = paymentsService.getPaymentsByGroup(groupId);
        return ResponseEntity.ok(payments);
    }

    /**
     * Update payment status.
     * 
     * @param paymentId the payment ID
     * @param status the new status
     * @return updated payment DTO
     */
    @PutMapping("/{paymentId}/status")
    @Operation(
        summary = "Update payment status",
        description = "Updates the status of a payment (e.g., COMPLETED, PENDING, FAILED)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @Parameter(description = "ID of the payment", required = true)
            @PathVariable Long paymentId,
            @Parameter(description = "New status", required = true)
            @RequestParam String status) {
        PaymentDTO payment = paymentsService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(payment);
    }
}
