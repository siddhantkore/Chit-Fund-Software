package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String month;

    @Column(name = "winning_amount", nullable = false)
    private double winningAmount;

    @Column(nullable = false)
    private double commission;

    @Column(name = "auction_date", nullable = false)
    private LocalDateTime auctionDate;

    @Column(length = 200)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winning_member_id", nullable = false)
    private User winningMemberId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
