package com.nival.chit.repository;

import com.nival.chit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides data access operations for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username.
     *
     * @param username the username
     * @return optional user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email the email address
     * @return optional user if found
     */
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT DISTINCT visibleUser
            FROM User visibleUser
            JOIN visibleUser.memberOf visibleMembership
            JOIN Membership currentMembership
              ON currentMembership.chitGroup.id = visibleMembership.chitGroup.id
            WHERE currentMembership.user.id = :userId
              AND currentMembership.status = com.nival.chit.enums.UserStatus.ACTIVE
              AND visibleMembership.status = com.nival.chit.enums.UserStatus.ACTIVE
            ORDER BY visibleUser.name ASC
            """)
    List<User> findVisibleUsersForMember(@Param("userId") Long userId);
}
