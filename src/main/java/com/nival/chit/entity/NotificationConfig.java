package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity for storing notification configuration rules per chit group.
 * Allows group admins to configure when and how notifications are sent
 * for various events like payment reminders, loan due dates, auctions, etc.
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification_config")
public class NotificationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The chit group this configuration applies to.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    /**
     * Type of notification event (e.g., "PAYMENT_REMINDER", "LOAN_DUE", "AUCTION_UPCOMING")
     */
    @Column(nullable = false, length = 50)
    private String notificationType;

    /**
     * Number of days before the event to send the notification.
     * For example, if payment date is 15th and advanceDays is 2, notification is sent on 13th.
     */
    @Column(name = "advance_days", nullable = false)
    private Integer advanceDays;

    /**
     * Whether this notification is enabled for the group.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Custom message template for this notification type.
     * Can include placeholders like {memberName}, {amount}, {date}, etc.
     */
    @Column(name = "message_template", length = 500)
    private String messageTemplate;

    /**
     * Day of month for recurring events (e.g., payment collection day).
     * Used for monthly payment reminders.
     */
    @Column(name = "recurring_day")
    private Integer recurringDay;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

