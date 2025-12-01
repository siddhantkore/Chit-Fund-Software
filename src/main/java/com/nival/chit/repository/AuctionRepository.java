package com.nival.chit.repository;

import com.nival.chit.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Auction entity.
 * Provides data access operations for auctions.
 */
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    /**
     * Find all auctions for a specific chit group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of all auctions in the group
     */
    @Query("SELECT a FROM Auction a WHERE a.chitGroup.id = :chitGroupId ORDER BY a.auctionDate DESC")
    List<Auction> findByChitGroupId(@Param("chitGroupId") Long chitGroupId);

    /**
     * Find all auctions won by a specific user.
     *
     * @param userId the ID of the user
     * @return list of auctions won by the user
     */
    @Query("SELECT a FROM Auction a WHERE a.winningMemberId.id = :userId ORDER BY a.auctionDate DESC")
    List<Auction> findByWinningMemberId(@Param("userId") Long userId);

    /**
     * Find auction by month and group.
     *
     * @param month the month
     * @param chitGroupId the ID of the chit group
     * @return optional auction if found
     */
    @Query("SELECT a FROM Auction a WHERE a.month = :month AND a.chitGroup.id = :chitGroupId")
    java.util.Optional<Auction> findByMonthAndChitGroupId(@Param("month") String month, @Param("chitGroupId") Long chitGroupId);
}

