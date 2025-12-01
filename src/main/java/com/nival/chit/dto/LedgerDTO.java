package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Ledger entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerDTO {
    private Long id;
    private String entryType; // CREDIT, DEBIT
    private Double amount;
    private LocalDateTime entryDate;
    private String remark;
    private String referenceId;
    private Long userId;
    private String userName;
    private Long chitGroupId;
    private String chitGroupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

