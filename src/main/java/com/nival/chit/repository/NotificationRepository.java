package com.nival.chit.repository;

import com.nival.chit.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entity.
 * Provides data access operations for user notifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all notifications for a specific user.
     *
     * @param userId the ID of the user
     * @return list of all notifications for the user
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") Long userId);

    /**
     * Find all notifications for a user by status (e.g., "UNREAD", "READ").
     *
     * @param userId the ID of the user
     * @param status the notification status
     * @return list of notifications matching the status
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.status = :status ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Find all notifications of a specific type for a user.
     *
     * @param userId the ID of the user
     * @param type the notification type
     * @return list of notifications matching the type
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.type = :type ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);

    /**
     * Count unread notifications for a user.
     *
     * @param userId the ID of the user
     * @return count of unread notifications
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.status = 'UNREAD'")
    Long countUnreadByUserId(@Param("userId") Long userId);
}

