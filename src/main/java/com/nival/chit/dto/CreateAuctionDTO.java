package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating an auction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuctionDTO {
    private String month;
    private Double winningAmount;
    private Double commission;
    private LocalDateTime auctionDate;
    private String remark;
    private Long winningMemberId;
    private Long chitGroupId;
}

