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

    /**
     * Principle represents the original amount of money that was loaned to the member.
     * It is stored as a double and is required (not null). The principle should be a positive value,
     * indicating the total amount borrowed by the member.
     */
    @Column(nullable = false)
    private double principle;

    /**
     * Interest represents the interest rate applied to the loan. It is stored as a double and is required (not null).
     * The interest rate should be a positive value, indicating the percentage of the loan amount that will be charged as interest.
     */
    @Column(nullable = false)
    private double interest;
    
    /**
     * Tenure represents the duration of the loan in months. It is stored as a double and is required (not null). The tenure should be a positive value, indicating the length of time over which the loan will be repaid.
     * This field is crucial for calculating the total amount payable by the member, as it determines the number of installments and the total interest accrued over the life of the loan.
     */
    @Column(nullable = false)
    private double tenure;

    /**
     * Current Payable represents the total amount that the member currently owes,
     * including both the principle and the interest. It is stored as a double and is required (not null).
     * The current payable should be a positive value, indicating the total amount that the member needs to repay.
     */
    @Column(name = "current_payable", nullable = false)
    private double currentPayable;

    /**
     * Start Date represents the date when the loan was initiated. It is stored as a LocalDateTime and is required (not null).
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * End Date represents the date when the loan is expected to be fully repaid.
     * It is stored as a LocalDateTime and is optional (can be null) because it may not be known at the time of loan creation.
     * The end date can be calculated based on the start date and tenure, or it can be updated later as the loan progresses.
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * Status represents the current state of the loan, such as "ACTIVE", "REPAID", or "DEFAULTED". It is stored as a string and is required (not null).
     * The length is limited to 20 characters to accommodate various statuses while ensuring efficient storage.
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * The member loan is associated with a specific chit group, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple member
     * loans can be linked to a single chit group. The fetch type is set to LAZY to optimize performance
     * by loading the chit group data only when it is accessed.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    /**
     * The member loan is associated with a specific user, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple member loans
     * can be linked to a single user. The fetch type is set to LAZY to optimize performance by loading the user data only when it is accessed.
     */
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
