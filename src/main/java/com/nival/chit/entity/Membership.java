package com.nival.chit.entity;

import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserStatus;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "membership")
@Data
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * The membership is associated with a specific user, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple memberships
     * can be linked to a single user. The fetch type is set to LAZY to optimize performance
     * by loading the user data only when it is accessed.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The membership is associated with a specific chit group, which is mandatory (not null).
     * This relationship is defined as a many-to-one association, meaning that multiple memberships
     * can be linked to a single chit group. The fetch type is set to LAZY to optimize performance
     * by loading the chit group data only when it is accessed.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "chit_group_id")
    private ChitGroup chitGroup;

    /**
     * Join Date represents the date when the user joined the chit group.
     * It is stored as a LocalDateTime and is required (not null).
     * This field is important for tracking the duration of the membership and can be used for various purposes,
     * such as calculating the total contributions made by the member or determining eligibility for certain benefits based on the length of membership.
     */
    private LocalDateTime joinDate;

    /**
     * Status represents the current state of the membership, such as "ACTIVE", "INACTIVE", or "SUSPENDED". It is stored as an enumerated type (UserStatus) and is required (not null).
     * The use of an enum ensures that only valid statuses can be assigned to a membership,
     */
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * Role represents the role of the user within the chit group, such as "MEMBER", "ADMIN", or "TREASURER". It is stored as an enumerated type (UserRoles) and is required (not null).
     * The use of an enum ensures that only valid roles can be assigned to a membership, which helps maintain data integrity and allows for role-based access control within the application.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "group_role")
    private GroupRole role;

    @PrePersist
    protected void onCreate() {
        if (joinDate == null) {
            joinDate = LocalDateTime.now();
        }
    }

}

