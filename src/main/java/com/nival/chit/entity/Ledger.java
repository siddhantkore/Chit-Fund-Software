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

    /**
     * Entry type indicates whether the ledger entry is a credit or a debit. It is stored as a string and is required (not null). The length is limited to 50 characters to accommodate various types of transactions (e.g., "CREDIT", "DEBIT", "ADJUSTMENT").
     */
    @Column(name = "entry_type", nullable = false, length = 50)
    private String entryType;  // e.g., "CREDIT", "DEBIT"

    /**
     * Amount represents the monetary value of the ledger entry. It is stored as a double and is required (not null). The amount should be positive for credits and negative for debits to maintain consistency in financial records.
     */
    @Column(nullable = false)
    private double amount;

    /**
     * The date and time when the ledger entry was created. This field is required (not null) and is stored as a LocalDateTime.
     */
    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    /**
     * Remark is an optional field that allows for additional information or notes about the ledger entry. It is stored as a string with a maximum length of 255 characters.
     * This can be used to provide context for the transaction, such as "Payment for auction #123" or "Adjustment for user #456".
     */
    @Column(length = 255)
    private String remark;

    @Column(name = "reference_id", length = 50)
    private String referenceId; // ID from related Auction or Payment

    /**
     * The ledger entry is associated with a specific user, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple ledger entries
     * can be linked to a single user. The fetch type is set to LAZY to optimize performance by loading the user data only when it is accessed.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The ledger entry is associated with a specific chit group, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple ledger entries can be linked to
     * a single chit group. The fetch type is set to LAZY to optimize performance by loading the chit group data only when it is accessed.
     */
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
