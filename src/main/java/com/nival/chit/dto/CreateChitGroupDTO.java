package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating a new chit group.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChitGroupDTO {
    private String name;
    private Integer duration; // in months
    private Double monthlyAmount;
    private Integer totalMembers;
    private LocalDateTime startDate;
}

