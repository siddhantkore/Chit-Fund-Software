package com.nival.chit.repository;

import com.nival.chit.entity.MemberLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MemberLoan entity.
 * Provides data access operations for member loans within chit groups.
 */
@Repository
public interface MemberLoanRepository extends JpaRepository<MemberLoan, Long> {

    /**
     * Find all loans for a specific chit group.
     * Used by group admins to view all active loans in their group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of all member loans in the specified group
     */
    @Query("SELECT ml FROM MemberLoan ml WHERE ml.chitGroup.id = :chitGroupId")
    List<MemberLoan> findByChitGroupId(@Param("chitGroupId") Long chitGroupId);

    /**
     * Find all loans for a specific user across all groups.
     * Used by members to view their total loan portfolio.
     *
     * @param userId the ID of the user
     * @return list of all loans belonging to the user
     */
    @Query("SELECT ml FROM MemberLoan ml WHERE ml.user.id = :userId")
    List<MemberLoan> findByUserId(@Param("userId") Long userId);

    /**
     * Find a specific loan for a user in a particular group.
     * Used to get detailed loan information for a member in a specific group.
     *
     * @param userId the ID of the user
     * @param chitGroupId the ID of the chit group
     * @return optional member loan if found
     */
    @Query("SELECT ml FROM MemberLoan ml WHERE ml.user.id = :userId AND ml.chitGroup.id = :chitGroupId")
    Optional<MemberLoan> findByUserIdAndChitGroupId(@Param("userId") Long userId, @Param("chitGroupId") Long chitGroupId);

    /**
     * Find all active loans (not completed/closed) for a user.
     *
     * @param userId the ID of the user
     * @return list of active member loans
     */
    @Query("SELECT ml FROM MemberLoan ml WHERE ml.user.id = :userId AND ml.status != 'CLOSED'")
    List<MemberLoan> findActiveLoansByUserId(@Param("userId") Long userId);
}

