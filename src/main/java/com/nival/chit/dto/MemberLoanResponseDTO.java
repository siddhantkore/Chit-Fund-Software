package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Member Loan responses.
 * Used to return loan information to clients without exposing internal entity details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoanResponseDTO {
    private Long id;
    private Double principle;
    private Double interestRate;
    private Double tenure;
    private Double currentPayable;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Long chitGroupId;
    private String chitGroupName;
    private String chitGroupCode;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

