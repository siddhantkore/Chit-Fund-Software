package com.nival.chit.services;

import com.nival.chit.dto.CreateNotificationConfigDTO;
import com.nival.chit.dto.NotificationConfigDTO;
import com.nival.chit.dto.NotificationResponseDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Notification;
import com.nival.chit.entity.NotificationConfig;
import com.nival.chit.entity.User;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.NotificationConfigRepository;
import com.nival.chit.repository.NotificationRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing notifications and notification configurations.
 * Handles creating, sending, and configuring notifications for chit groups.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationConfigRepository notificationConfigRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final UserRepository userRepository;

    /**
     * Create a new notification configuration for a chit group.
     *
     * @param createDTO the notification configuration data
     * @return the created notification config DTO
     */
    @Transactional
    public NotificationConfigDTO createNotificationConfig(CreateNotificationConfigDTO createDTO) {
        ChitGroup chitGroup = chitGroupRepository.findById(createDTO.getChitGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found"));

        // Check if config already exists for this type
        Optional<NotificationConfig> existing = notificationConfigRepository
                .findByChitGroupIdAndNotificationType(createDTO.getChitGroupId(), createDTO.getNotificationType());

        NotificationConfig config;
        if (existing.isPresent()) {
            // Update existing config
            config = existing.get();
            config.setAdvanceDays(createDTO.getAdvanceDays());
            config.setEnabled(createDTO.getEnabled() != null ? createDTO.getEnabled() : true);
            config.setMessageTemplate(createDTO.getMessageTemplate());
            config.setRecurringDay(createDTO.getRecurringDay());
        } else {
            // Create new config
            config = new NotificationConfig();
            config.setChitGroup(chitGroup);
            config.setNotificationType(createDTO.getNotificationType());
            config.setAdvanceDays(createDTO.getAdvanceDays());
            config.setEnabled(createDTO.getEnabled() != null ? createDTO.getEnabled() : true);
            config.setMessageTemplate(createDTO.getMessageTemplate());
            config.setRecurringDay(createDTO.getRecurringDay());
        }

        config = notificationConfigRepository.save(config);
        return convertToDTO(config);
    }

    /**
     * Get all notification configurations for a chit group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of notification config DTOs
     */
    @Transactional(readOnly = true)
    public List<NotificationConfigDTO> getNotificationConfigsByGroup(Long chitGroupId) {
        List<NotificationConfig> configs = notificationConfigRepository.findByChitGroupId(chitGroupId);
        return configs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update a notification configuration.
     *
     * @param configId the ID of the notification config
     * @param updateDTO the updated configuration data
     * @return the updated notification config DTO
     */
    @Transactional
    public NotificationConfigDTO updateNotificationConfig(Long configId, CreateNotificationConfigDTO updateDTO) {
        NotificationConfig config = notificationConfigRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Notification config not found"));

        if (updateDTO.getAdvanceDays() != null) {
            config.setAdvanceDays(updateDTO.getAdvanceDays());
        }
        if (updateDTO.getEnabled() != null) {
            config.setEnabled(updateDTO.getEnabled());
        }
        if (updateDTO.getMessageTemplate() != null) {
            config.setMessageTemplate(updateDTO.getMessageTemplate());
        }
        if (updateDTO.getRecurringDay() != null) {
            config.setRecurringDay(updateDTO.getRecurringDay());
        }

        config = notificationConfigRepository.save(config);
        return convertToDTO(config);
    }

    /**
     * Create and send a notification to a user.
     *
     * @param userId the ID of the user
     * @param message the notification message
     * @param type the notification type
     * @return the created notification DTO
     */
    @Transactional
    public NotificationResponseDTO createNotification(Long userId, String message, String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus("UNREAD");

        notification = notificationRepository.save(notification);
        log.info("Notification created for user {}: {}", userId, message);

        return convertToResponseDTO(notification);
    }

    /**
     * Get all notifications for a user.
     *
     * @param userId the ID of the user
     * @return list of notification response DTOs
     */
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get unread notifications for a user.
     *
     * @param userId the ID of the user
     * @return list of unread notification response DTOs
     */
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getUnreadNotificationsByUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndStatus(userId, "UNREAD");
        return notifications.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mark a notification as read.
     *
     * @param notificationId the ID of the notification
     * @return the updated notification DTO
     */
    @Transactional
    public NotificationResponseDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        notification.setStatus("READ");
        notification = notificationRepository.save(notification);

        return convertToResponseDTO(notification);
    }

    /**
     * Get count of unread notifications for a user.
     *
     * @param userId the ID of the user
     * @return count of unread notifications
     */
    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    /**
     * Convert NotificationConfig entity to DTO.
     */
    private NotificationConfigDTO convertToDTO(NotificationConfig config) {
        return NotificationConfigDTO.builder()
                .id(config.getId())
                .chitGroupId(config.getChitGroup().getId())
                .notificationType(config.getNotificationType())
                .advanceDays(config.getAdvanceDays())
                .enabled(config.getEnabled())
                .messageTemplate(config.getMessageTemplate())
                .recurringDay(config.getRecurringDay())
                .createdAt(config.getCreatedAt())
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    /**
     * Convert Notification entity to Response DTO.
     */
    private NotificationResponseDTO convertToResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .userId(notification.getUser().getId())
                .userName(notification.getUser().getName())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

