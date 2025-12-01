package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for NotificationConfig.
 * Used for creating and updating notification configuration rules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfigDTO {
    private Long id;
    private Long chitGroupId;
    private String notificationType;
    private Integer advanceDays;
    private Boolean enabled;
    private String messageTemplate;
    private Integer recurringDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

