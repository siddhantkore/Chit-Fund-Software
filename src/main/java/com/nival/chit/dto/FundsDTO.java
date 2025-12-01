package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Funds entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundsDTO {
    private Long id;
    private String name;
    private Double totalAmount;
    private Integer cyclePeriod;
    private Double interestRate;
    private Long chitGroupId;
    private String chitGroupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

