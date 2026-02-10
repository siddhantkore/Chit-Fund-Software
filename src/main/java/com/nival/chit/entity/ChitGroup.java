package com.nival.chit.entity;

import com.nival.chit.enums.ChitGroupStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "chit_group")
@EntityListeners(AuditingEntityListener.class)
public class ChitGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;


    @Column(unique = true, nullable = false, updatable = false)
    private String groupCode;

    private String name;

    private int duration;

    private double monthlyAmount;

    private int totalMembers;

    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    private ChitGroupStatus status;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "fund_id")
    private Funds fundId;

    /**
     * The memberships field represents the list of memberships associated with this chit group.
     * It is a one-to-many relationship, where one chit group can have multiple memberships.
     * The cascade type ALL ensures that any changes to the chit group will also affect the associated memberships,
     * and orphanRemoval = true ensures that if a membership is removed from the chit group, it will also be deleted from the database.
     * This field allows us to easily access all the members who are part of this chit group and manage their membership details effectively.
     */
    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;

    /**
     * The auctions field represents the list of auctions associated with this chit group.
     * It is a one-to-many relationship, where one chit group can have multiple auctions.
     * The cascade type ALL ensures that any changes to the chit group will also affect the associated auctions,
     * and orphanRemoval = true ensures that if an auction is removed from the chit group, it will also be deleted from the database.
     * This field allows us to easily access all the auctions that are part of this chit group and manage their details effectively.
     */
    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auction> auction;

    /**
     * 
     */
    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberLoan> memberLoan;

    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ledger> ledger;

    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payments> payments;

    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationConfig> notificationConfigs;

    @PrePersist
    protected void onCreate() {
        this.groupCode = generateGroupCode();
    }

    private String generateGroupCode() {
        String prefix = "CHIT";
        String randomPart = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return prefix + randomPart;  // e.g. CHIT3F5A7C
    }


}
