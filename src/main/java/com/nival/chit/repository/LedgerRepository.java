package com.nival.chit.repository;

import com.nival.chit.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Ledger entity.
 * Provides data access operations for ledger entries.
 */
@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    /**
     * Find all ledger entries for a specific chit group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of ledger entries ordered by entry date
     */
    @Query("SELECT l FROM Ledger l WHERE l.chitGroup.id = :chitGroupId ORDER BY l.entryDate ASC")
    List<Ledger> findByChitGroupId(@Param("chitGroupId") Long chitGroupId);

    /**
     * Find all ledger entries for a specific user.
     *
     * @param userId the ID of the user
     * @return list of ledger entries ordered by entry date
     */
    @Query("SELECT l FROM Ledger l WHERE l.user.id = :userId ORDER BY l.entryDate ASC")
    List<Ledger> findByUserId(@Param("userId") Long userId);

    /**
     * Find all ledger entries for a user in a specific group.
     *
     * @param userId the ID of the user
     * @param chitGroupId the ID of the chit group
     * @return list of ledger entries ordered by entry date
     */
    @Query("SELECT l FROM Ledger l WHERE l.user.id = :userId AND l.chitGroup.id = :chitGroupId ORDER BY l.entryDate ASC")
    List<Ledger> findByUserIdAndChitGroupId(@Param("userId") Long userId, @Param("chitGroupId") Long chitGroupId);
}
