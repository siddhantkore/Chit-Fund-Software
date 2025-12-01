package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a single chat message inside a chit group's private chat.
 *
 * <p>Only members of the associated chit group are allowed to send and read messages.</p>
 */
@Entity
@Table(name = "group_chat_message")
@EntityListeners(AuditingEntityListener.class)
@Data
public class GroupChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The chit group where this message was sent.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    /**
     * The user who sent the message.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * Plain text content of the chat message.
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /**
     * Optional message type for future extension (e.g. TEXT, IMAGE, SYSTEM).
     */
    @Column(name = "message_type", length = 20)
    private String messageType = "TEXT";

    /**
     * Server-side timestamp when the message was created.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}


