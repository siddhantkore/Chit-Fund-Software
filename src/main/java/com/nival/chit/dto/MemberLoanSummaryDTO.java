package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for summarizing a member's loan in a specific group.
 * Provides a concise view of loan status and current payable amount.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoanSummaryDTO {
    private Long loanId;
    private Long chitGroupId;
    private String chitGroupName;
    private String chitGroupCode;
    private Double principle;
    private Double currentPayable;
    private String status;
    private Long daysRemaining;
}

