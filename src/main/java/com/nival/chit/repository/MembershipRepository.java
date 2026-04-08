package com.nival.chit.repository;

import com.nival.chit.entity.Membership;
import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for memberships linking users to chit groups.
 */
@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    /**
     * All memberships for a specific user across chit groups.
     */
    @Query("SELECT m FROM Membership m WHERE m.user.id = :userId")
    List<Membership> findByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Membership m WHERE m.user.id = :userId AND m.status = :status")
    List<Membership> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") UserStatus status);

    /**
     * All memberships in a chit group.
     */
    @Query("SELECT m FROM Membership m WHERE m.chitGroup.id = :groupId")
    List<Membership> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT m FROM Membership m WHERE m.chitGroup.id = :groupId AND m.status = :status")
    List<Membership> findByGroupIdAndStatus(@Param("groupId") Long groupId, @Param("status") UserStatus status);

    /**
     * Find membership by user and group.
     */
    @Query("SELECT m FROM Membership m WHERE m.user.id = :userId AND m.chitGroup.id = :groupId")
    Optional<Membership> findByUserIdAndGroupId(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query("""
            SELECT m FROM Membership m
            WHERE m.user.id = :userId
              AND m.chitGroup.id = :groupId
              AND m.status = :status
            """)
    Optional<Membership> findByUserIdAndGroupIdAndStatus(
            @Param("userId") Long userId,
            @Param("groupId") Long groupId,
            @Param("status") UserStatus status
    );

    @Query("""
            SELECT m FROM Membership m
            WHERE m.chitGroup.id = :groupId
              AND m.role = :role
              AND m.status = :status
            """)
    List<Membership> findByGroupIdAndRoleAndStatus(
            @Param("groupId") Long groupId,
            @Param("role") GroupRole role,
            @Param("status") UserStatus status
    );
}

