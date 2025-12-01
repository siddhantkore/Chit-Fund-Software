package com.nival.chit.dto;

import com.nival.chit.enums.ChitGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ChitGroup entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChitGroupDTO {
    private Long id;
    private String groupCode;
    private String name;
    private Integer duration;
    private Double monthlyAmount;
    private Integer totalMembers;
    private LocalDateTime startDate;
    private ChitGroupStatus status;
    private Long fundId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

