package com.nival.chit.dto;

import com.nival.chit.enums.GroupRole;
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
    private Double monthlyAmount;
    private Integer duration;
    private Integer totalMembers;
    private LocalDateTime startDate;
    private LocalDateTime joinDate;
    private UserStatus status;
    private GroupRole role;
}
