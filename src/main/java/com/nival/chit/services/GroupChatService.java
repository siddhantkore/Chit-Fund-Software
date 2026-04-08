package com.nival.chit.services;

import com.nival.chit.dto.GroupChatMessageDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.GroupChatMessage;
import com.nival.chit.entity.User;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.GroupChatMessageRepository;
import com.nival.chit.security.AccessControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing private group chat messages for chit groups.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupChatService {

    private final GroupChatMessageRepository groupChatMessageRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final AccessControlService accessControlService;

    /**
     * Send a chat message in a chit group's private chat.
     * Only members of the group should be allowed to call this in the future
     * once security and membership checks are wired in.
     *
     * @param chitGroupId id of the chit group
     * @param senderId id of the sending user
     * @param content message content
     * @return saved message DTO
     */
    @Transactional
    public GroupChatMessageDTO sendMessage(Long chitGroupId, String content) {
        User sender = accessControlService.getCurrentUser();
        accessControlService.requireActiveGroupMembership(chitGroupId);
        ChitGroup chitGroup = chitGroupRepository.findById(chitGroupId)
                .orElseThrow(() -> {
                    log.warn("Attempted to send message to non-existent group: {}", chitGroupId);
                    return new IllegalArgumentException("Chit group not found");
                });

        GroupChatMessage message = new GroupChatMessage();
        message.setChitGroup(chitGroup);
        message.setSender(sender);
        message.setContent(content);
        message.setMessageType("TEXT");

        GroupChatMessage saved = groupChatMessageRepository.save(message);
        log.info("Chat message sent in group {} by user {}", chitGroupId, sender.getId());

        return toDto(saved);
    }

    /**
     * Get all messages for a chit group, ordered by creation time.
     *
     * @param chitGroupId id of the chit group
     * @return list of message DTOs
     */
    @Transactional(readOnly = true)
    public List<GroupChatMessageDTO> getMessagesForGroup(Long chitGroupId) {
        accessControlService.requireActiveGroupMembership(chitGroupId);
        List<GroupChatMessage> messages =
                groupChatMessageRepository.findByChitGroupIdOrderByCreatedAtAsc(chitGroupId);

        return messages.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get recent messages for a chit group after a specific message id.
     * Useful for polling or incremental loading in the client.
     *
     * @param chitGroupId id of the chit group
     * @param lastMessageId last seen message id (optional)
     * @return list of recent message DTOs
     */
    @Transactional(readOnly = true)
    public List<GroupChatMessageDTO> getRecentMessages(Long chitGroupId, Long lastMessageId) {
        accessControlService.requireActiveGroupMembership(chitGroupId);
        List<GroupChatMessage> messages =
                groupChatMessageRepository.findRecentMessages(chitGroupId, lastMessageId);

        return messages.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GroupChatMessageDTO toDto(GroupChatMessage message) {
        return GroupChatMessageDTO.builder()
                .id(message.getId())
                .chitGroupId(message.getChitGroup().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

