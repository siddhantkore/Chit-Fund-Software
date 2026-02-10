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

    /**
     * The month field represents the month for which the auction is conducted, stored as a
     * string (e.g., "January", "February", etc.). This allows for easy identification of the auction period.
     */
    @Column(nullable = false, length = 20)
    private String month;

    /**
     * The winningAmount field represents the amount won by the winning member in the auction.
     * It is calculated based on the total fund amount, interest, and commission for that month.
     * This field is crucial for determining the payout to the winning member after deducting the commission and adding any applicable interest.
     */
    @Column(name = "winning_amount", nullable = false)
    private double winningAmount;

    /**
     * The commission field represents the amount deducted as commission from the winning amount.
     * It is calculated as a percentage of the winning amount and is stored in this field for record-keeping and financial calculations related to the auction.
     * This allows for transparent tracking of the commission earned by the chit fund management from each auction.
     */
    @Column(nullable = false)
    private double commission;

    /**
     * The auctionDate field represents the date and time when the auction is conducted.
     * It is crucial for scheduling and tracking the auctions for each month.
     * This field allows the system to determine when to conduct the auction and ensures that it is done in a timely manner according to the schedule of the chit group.
     */
    @Column(name = "auction_date", nullable = false)
    private LocalDateTime auctionDate;

    /**
     * The remark field allows for any additional notes or comments related to the auction.
     * This can include special circumstances, observations, or any relevant information that may be useful for record-keeping or future reference regarding the auction.
     * It provides flexibility for administrators to document any important details about the auction process or outcomes.
     */
    @Column(length = 200)
    private String remark;

    /**
     * The winningMemberId field represents the user who won the auction for that month.
     * It is a foreign key reference to the User entity, allowing us to identify which member won the auction
     * and is eligible for the payout based on the winning amount calculated for that month.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "winning_member_id", nullable = false)
    private User winningMemberId;

    /**
     * The chitGroup field represents the chit group to which this auction belongs.
     * It is a many-to-one relationship,
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chit_group_id", nullable = false)
    private ChitGroup chitGroup;

    /**
     * The createdAt field is automatically set to the current timestamp when a new auction record is created.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * The updatedAt field is automatically updated with the current timestamp whenever the auction record is updated.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
