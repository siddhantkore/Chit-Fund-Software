package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new notification configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationConfigDTO {
    private Long chitGroupId;
    private String notificationType;
    private Integer advanceDays;
    private Boolean enabled;
    private String messageTemplate;
    private Integer recurringDay;
}

