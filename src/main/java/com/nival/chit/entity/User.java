package com.nival.chit.entity;

import com.nival.chit.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    /**
     * Username is a unique identifier for the user, used for login and display purposes.I
     * t must be unique across all users and is required (not null). The length is limited to 15 characters to ensure concise usernames.
     */
    @Column(name = "username", unique = true, nullable = false, length = 15)
    private String username;

    /**
     * Phone number of the user. It is stored as a long integer and is optional (nullable). The length is limited to 10 digits
     */
    @Column(length = 10)
    private long phone;

    /**
     * Email is a unique identifier for the user, used for login and communication purposes.
     * It must be unique across all users and is required (not null). The length is limited to 255 characters to accommodate typical email addresses.
     */
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    /**
     * Password is required for user authentication.
     * It is stored as a string and is required (not null).
     * The length is limited to 100 characters to accommodate hashed passwords.
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * Role of the user in the system, defined as an enum (e.g., ADMIN, USER).
     * It is stored as a string in the database and is required (not null).
     */
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

   @PrePersist
   protected void onCreate() {
       createdAt = LocalDateTime.now();
       updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
       updatedAt = LocalDateTime.now();
   }


    /**
     * One-to-many relationship with Membership. A user can be a member of multiple
     * chit groups, and each membership record links a user to a specific chit group.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberOf;

    /**
     * One-to-many relationship with Notification. A user can receive multiple notifications,
     * and each notification is linked to a specific user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    /**
     * One-to-many relationship with MemberLoan. A user can have multiple loans as a
     * member,
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberLoan> memberLoans;

    /**
     * One-to-many relationship with Auction. A user can win multiple auctions, and each auction record links a winning user to a specific auction event.
     */
    @OneToMany(mappedBy = "winningMemberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auction> auctions;

    /**
     * One-to-many relationship with Payments. A user can make multiple payments,
     * and each payment record links a user to a specific payment transaction.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payments> payments;

    /**
     * One-to-many relationship with Ledger. A user can have multiple ledger entries,
     * and each ledger entry is linked to a specific user for tracking financial transactions and balances.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ledger> ledgers;


}