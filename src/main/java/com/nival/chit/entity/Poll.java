package com.nival.chit.entity;

import com.nival.chit.enums.PollStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "polls")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chit_group_id")
    private ChitGroup chitGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private User creator;

    @Column(nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    private PollStatus status;

    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
