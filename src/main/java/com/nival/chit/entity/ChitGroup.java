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

    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships;

    @OneToMany(mappedBy = "chitGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auction> auction;

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
