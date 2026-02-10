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
@Table(name = "payments")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Month represents the month for which the payment is made. 
     * It is stored as a string and is required (not null).
     * The length is limited to 15 characters to accommodate various formats of month representation 
     * (e.g., "January", "Jan", "2024-01").
     * This field is crucial for tracking payments on a monthly basis and can be used for generating reports, calculating dues, and managing financial records.
     */
    @Column(nullable = false, length = 15)
    private String month;

    /**
     * Amount Paid represents the total amount of money that the member has paid for the specified month. 
     * It is stored as a double and is required (not null). The amount should be a positive value, indicating the total
     * payment made by the member for that month. This field is essential for tracking payment history, calculating outstanding dues, and managing financial records for the chit group.
     */
    @Column(name = "amount_paid", nullable = false)
    private double amountPaid;

    /**
     * Payment Date represents the date when the payment was made. It is stored as a LocalDateTime and is required (not null).
     * This field is important for tracking the timing of payments, generating payment history, and managing financial records for the chit group.
     */
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    /**
     * Mode represents the method of payment used by the member, such as "CASH", "CHEQUE", "ONLINE_TRANSFER", etc.
     * It is stored as a string and is required (not null). The length is limited to 20 characters to accommodate various payment methods while
     * ensuring efficient storage.
     */
    @Column(nullable = false, length = 20)
    private String mode;

    /**
     * Status represents the current state of the payment, such as "PENDING", "COMPLETED", or "FAILED". It is stored as a string and is required (not null).
     * The length is limited to 20 characters to accommodate various statuses while ensuring efficient storage.
     * This field is crucial for tracking the progress of payments, managing financial records, and generating reports for the chit group.
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * The payment is associated with a specific user, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple payments can be linked to a single user.
     * The fetch type is set to LAZY to optimize performance by loading the user data only when it is accessed.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The payment is associated with a specific chit group, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple payments can be linked to a single chit group.
     * The fetch type is set to LAZY to optimize performance by loading the chit group data only when it is accessed.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
