package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "funds")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Funds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "cycle_period", nullable = false)
    private int cyclePeriod;

    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "chit_group_id", unique = true)
    private ChitGroup chitGroup;


}
