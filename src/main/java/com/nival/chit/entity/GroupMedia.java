package com.nival.chit.entity;

import com.nival.chit.enums.MediaType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_media")
@Data
@EntityListeners(AuditingEntityListener.class)
public class GroupMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chit_group_id")
    private ChitGroup chitGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "uploaded_by")
    private User uploader;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String caption;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
