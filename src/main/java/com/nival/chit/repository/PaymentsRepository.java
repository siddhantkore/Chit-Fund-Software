package com.nival.chit.repository;

import com.nival.chit.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for member payments (monthly contributions and loan repayments).
 */
@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    /**
     * Sum of all completed payments for a chit group up to a given date.
     */
    @Query("""
            SELECT COALESCE(SUM(p.amountPaid), 0)
            FROM Payments p
            WHERE p.chitGroup.id = :chitGroupId
              AND p.status = 'COMPLETED'
              AND p.paymentDate <= :asOf
            """)
    double sumCompletedByGroupUpTo(
            @Param("chitGroupId") Long chitGroupId,
            @Param("asOf") LocalDateTime asOf
    );

    /**
     * Sum of all completed payments for a specific user in a chit group up to a given date.
     */
    @Query("""
            SELECT COALESCE(SUM(p.amountPaid), 0)
            FROM Payments p
            WHERE p.chitGroup.id = :chitGroupId
              AND p.user.id = :userId
              AND p.status = 'COMPLETED'
              AND p.paymentDate <= :asOf
            """)
    double sumUserCompletedByGroupUpTo(
            @Param("chitGroupId") Long chitGroupId,
            @Param("userId") Long userId,
            @Param("asOf") LocalDateTime asOf
    );

    /**
     * List of payments for a user in a chit group.
     */
    @Query("""
            SELECT p FROM Payments p
            WHERE p.chitGroup.id = :chitGroupId
              AND p.user.id = :userId
            ORDER BY p.paymentDate ASC
            """)
    List<Payments> findByGroupAndUser(
            @Param("chitGroupId") Long chitGroupId,
            @Param("userId") Long userId
    );
}


