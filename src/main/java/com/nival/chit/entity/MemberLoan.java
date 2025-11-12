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
@Table(name = "member_loan")
public class MemberLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double principle;

    @Column(nullable = false)
    private double interest;

    @Column(nullable = false)
    private double tenure;

    @Column(name = "current_payable", nullable = false)
    private double currentPayable;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable = false, length = 20)
    private String status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
