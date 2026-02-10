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

    /**
     * Name of the fund, typically the same as the chit group name for easy identification.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The total amount represents the total fund amount for the chit group, calculated as:
     * totalAmount = monthlyAmount * totalMembers * duration
     */
    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    /**
     * The cycle period represents the number of months for the chit group cycle.
     */
    @Column(name = "cycle_period", nullable = false)
    private int cyclePeriod;

    /**
     * The interest rate is stored as a percentage (e.g., 5.0 for 5% interest).
     */
    @Column(name = "interest_rate", nullable = false)
    private double interestRate;

    /**
     * At the creation of a fund, the createdAt field is automatically set to the current timestamp.
     */
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * The updatedAt field is automatically updated with the current timestamp
     * whenever the entity is updated.
     */
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * One-to-one relationship with ChitGroup.
     * Each fund is associated with exactly one chit group,
     * and each chit group has exactly one fund.
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "chit_group_id", unique = true)
    private ChitGroup chitGroup;


}
