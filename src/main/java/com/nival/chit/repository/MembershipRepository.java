package com.nival.chit.repository;

import com.nival.chit.entity.Membership;
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

    /**
     * All memberships in a chit group.
     */
    @Query("SELECT m FROM Membership m WHERE m.chitGroup.id = :groupId")
    List<Membership> findByGroupId(@Param("groupId") Long groupId);

    /**
     * Find membership by user and group.
     */
    @Query("SELECT m FROM Membership m WHERE m.user.id = :userId AND m.chitGroup.id = :groupId")
    Optional<Membership> findByUserIdAndGroupId(@Param("userId") Long userId, @Param("groupId") Long groupId);
}


