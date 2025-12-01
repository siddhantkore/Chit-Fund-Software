package com.nival.chit.dto;

import com.nival.chit.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Membership entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long chitGroupId;
    private String chitGroupName;
    private String chitGroupCode;
    private LocalDateTime joinDate;
    private UserStatus status;
}

