package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating a payment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDTO {
    private String month;
    private Double amountPaid;
    private LocalDateTime paymentDate;
    private String mode; // CASH, BANK_TRANSFER, UPI, etc.
    private Long userId;
    private Long chitGroupId;
}

