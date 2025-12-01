package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Payment entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String month;
    private Double amountPaid;
    private LocalDateTime paymentDate;
    private String mode;
    private String status;
    private Long userId;
    private String userName;
    private Long chitGroupId;
    private String chitGroupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

