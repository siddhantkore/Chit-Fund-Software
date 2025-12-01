package com.nival.chit.controllers;

import com.nival.chit.dto.CreateNotificationConfigDTO;
import com.nival.chit.dto.NotificationConfigDTO;
import com.nival.chit.dto.NotificationResponseDTO;
import com.nival.chit.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing notifications and notification configurations.
 * 
 * <p>Provides endpoints for:</p>
 * <ul>
 *     <li>Group admins to configure notification rules (advance days, message templates)</li>
 *     <li>Users to view their notifications and mark them as read</li>
 *     <li>Managing notification preferences per chit group</li>
 * </ul>
 * 
 * <p>Notification types include: PAYMENT_REMINDER, LOAN_DUE, AUCTION_UPCOMING, LOAN_APPROVAL, etc.</p>
 */
@RestController
@RequestMapping("/api/chit/notifications/")
@Tag(name = "Notifications", description = "Configure and inspect notifications sent by the platform.")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Create or update a notification configuration for a chit group.
     * Used by group admins to set up notification rules (e.g., send payment reminder 2 days before due date).
     * 
     * @param createDTO the notification configuration data
     * @return the created/updated notification config
     */
    @PostMapping("/config")
    @Operation(
        summary = "Create or update notification configuration",
        description = "Creates a new notification configuration or updates existing one for a chit group. " +
                     "Allows admins to configure advance days (e.g., 2 days before payment due date), " +
                     "message templates, and enable/disable notifications."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully created/updated notification config",
            content = @Content(schema = @Schema(implementation = NotificationConfigDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<NotificationConfigDTO> createOrUpdateNotificationConfig(
            @RequestBody CreateNotificationConfigDTO createDTO) {
        NotificationConfigDTO config = notificationService.createNotificationConfig(createDTO);
        return ResponseEntity.ok(config);
    }

    /**
     * Get all notification configurations for a chit group.
     * Used by group admins to view and manage notification settings.
     * 
     * @param groupId the ID of the chit group
     * @return list of notification configurations for the group
     */
    @GetMapping("/config/group/{groupId}")
    @Operation(
        summary = "Get notification configurations for a group",
        description = "Retrieves all notification configurations for a specific chit group. " +
                     "Shows all configured notification types, advance days, and message templates."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved notification configs",
            content = @Content(schema = @Schema(implementation = NotificationConfigDTO.class))
        )
    })
    public ResponseEntity<List<NotificationConfigDTO>> getNotificationConfigsByGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId) {
        List<NotificationConfigDTO> configs = notificationService.getNotificationConfigsByGroup(groupId);
        return ResponseEntity.ok(configs);
    }

    /**
     * Update an existing notification configuration.
     * 
     * @param configId the ID of the notification config to update
     * @param updateDTO the updated configuration data
     * @return the updated notification config
     */
    @PutMapping("/config/{configId}")
    @Operation(
        summary = "Update notification configuration",
        description = "Updates an existing notification configuration. " +
                     "Can modify advance days, message template, enabled status, and recurring day."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully updated notification config",
            content = @Content(schema = @Schema(implementation = NotificationConfigDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Notification config not found")
    })
    public ResponseEntity<NotificationConfigDTO> updateNotificationConfig(
            @Parameter(description = "ID of the notification config", required = true)
            @PathVariable Long configId,
            @RequestBody CreateNotificationConfigDTO updateDTO) {
        NotificationConfigDTO config = notificationService.updateNotificationConfig(configId, updateDTO);
        return ResponseEntity.ok(config);
    }

    /**
     * Get all notifications for a user.
     * Returns both read and unread notifications, sorted by creation date (newest first).
     * 
     * @param userId the ID of the user
     * @return list of all notifications for the user
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get all notifications for a user",
        description = "Retrieves all notifications (read and unread) for a specific user. " +
                     "Notifications are sorted by creation date, newest first."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved notifications",
            content = @Content(schema = @Schema(implementation = NotificationResponseDTO.class))
        )
    })
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for a user.
     * 
     * @param userId the ID of the user
     * @return list of unread notifications
     */
    @GetMapping("/user/{userId}/unread")
    @Operation(
        summary = "Get unread notifications for a user",
        description = "Retrieves only unread notifications for a specific user. " +
                     "Useful for showing notification badges or unread counts."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved unread notifications",
            content = @Content(schema = @Schema(implementation = NotificationResponseDTO.class))
        )
    })
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotificationsByUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        List<NotificationResponseDTO> notifications = notificationService.getUnreadNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get count of unread notifications for a user.
     * Useful for displaying notification badges.
     * 
     * @param userId the ID of the user
     * @return count of unread notifications
     */
    @GetMapping("/user/{userId}/unread/count")
    @Operation(
        summary = "Get unread notification count",
        description = "Returns the count of unread notifications for a user. " +
                     "Useful for displaying notification badges in the UI."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved unread count")
    })
    public ResponseEntity<Long> getUnreadCount(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Mark a notification as read.
     * 
     * @param notificationId the ID of the notification
     * @return the updated notification
     */
    @PutMapping("/{notificationId}/read")
    @Operation(
        summary = "Mark notification as read",
        description = "Marks a specific notification as read. " +
                     "Updates the notification status from UNREAD to READ."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully marked notification as read",
            content = @Content(schema = @Schema(implementation = NotificationResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @Parameter(description = "ID of the notification", required = true)
            @PathVariable Long notificationId) {
        NotificationResponseDTO notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }
}
