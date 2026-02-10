package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"poll_id", "user_id"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private PollOption option;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "voted_at", updatable = false)
    private LocalDateTime votedAt;
}
