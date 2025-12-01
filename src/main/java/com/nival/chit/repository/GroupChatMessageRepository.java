package com.nival.chit.repository;

import com.nival.chit.entity.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing group chat messages.
 */
@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {

    /**
     * Find all messages for a specific chit group ordered by creation time.
     *
     * @param chitGroupId the chit group id
     * @return list of messages
     */
    @Query("SELECT m FROM GroupChatMessage m WHERE m.chitGroup.id = :chitGroupId ORDER BY m.createdAt ASC")
    List<GroupChatMessage> findByChitGroupIdOrderByCreatedAtAsc(@Param("chitGroupId") Long chitGroupId);

    /**
     * Find recent messages for a chit group, limited by a max id or timestamp if needed.
     *
     * @param chitGroupId the chit group id
     * @param lastMessageId optional last message id to page after
     * @return list of messages
     */
    @Query("""
            SELECT m FROM GroupChatMessage m
            WHERE m.chitGroup.id = :chitGroupId
              AND (:lastMessageId IS NULL OR m.id > :lastMessageId)
            ORDER BY m.createdAt ASC
            """)
    List<GroupChatMessage> findRecentMessages(
            @Param("chitGroupId") Long chitGroupId,
            @Param("lastMessageId") Long lastMessageId
    );
}


