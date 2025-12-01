package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Auction entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDTO {
    private Long id;
    private String month;
    private Double winningAmount;
    private Double commission;
    private LocalDateTime auctionDate;
    private String remark;
    private Long winningMemberId;
    private String winningMemberName;
    private Long chitGroupId;
    private String chitGroupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

