package com.nival.chit.repository;

import com.nival.chit.entity.NotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for NotificationConfig entity.
 * Provides data access operations for notification configuration rules.
 */
@Repository
public interface NotificationConfigRepository extends JpaRepository<NotificationConfig, Long> {

    /**
     * Find all notification configurations for a specific chit group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of all notification configs for the group
     */
    @Query("SELECT nc FROM NotificationConfig nc WHERE nc.chitGroup.id = :chitGroupId")
    List<NotificationConfig> findByChitGroupId(@Param("chitGroupId") Long chitGroupId);

    /**
     * Find a specific notification configuration by group and type.
     *
     * @param chitGroupId the ID of the chit group
     * @param notificationType the type of notification
     * @return optional notification config if found
     */
    @Query("SELECT nc FROM NotificationConfig nc WHERE nc.chitGroup.id = :chitGroupId AND nc.notificationType = :notificationType")
    Optional<NotificationConfig> findByChitGroupIdAndNotificationType(
            @Param("chitGroupId") Long chitGroupId,
            @Param("notificationType") String notificationType);

    /**
     * Find all enabled notification configurations for a chit group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of enabled notification configs
     */
    @Query("SELECT nc FROM NotificationConfig nc WHERE nc.chitGroup.id = :chitGroupId AND nc.enabled = true")
    List<NotificationConfig> findEnabledByChitGroupId(@Param("chitGroupId") Long chitGroupId);
}

