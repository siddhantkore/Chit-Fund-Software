package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Message represents the content of the notification that will be sent to the user.
     * It is stored as a string and is required (not null). The length is limited to 250 characters to ensure concise and clear communication while optimizing storage.
     */
    @Column(nullable = false, length = 250)
    private String message;

    /**
     * Type represents the category or nature of the notification, such as "INFO", "ALERT", or "REMINDER". It is stored as a string and is required (not null).
     * The length is limited to 30 characters to accommodate various types of notifications while ensuring efficient storage.
     */
    @Column(nullable = false, length = 30)
    private String type;

    /**
     * Status represents whether the notification has been read or is still unread.
     * It is stored as a string and is required (not null).
     * The length is limited to 20 characters to accommodate various statuses while ensuring efficient storage.
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * The date and time when the notification was created. This field is required (not null) and is stored as a LocalDateTime.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
