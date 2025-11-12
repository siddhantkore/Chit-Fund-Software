package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ledger")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "entry_type", nullable = false, length = 50)
    private String entryType;  // e.g., "CREDIT", "DEBIT"

    @Column(nullable = false)
    private double amount;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(length = 255)
    private String remark;

    @Column(name = "reference_id", length = 50)
    private String referenceId; // ID from related Auction or Payment

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
