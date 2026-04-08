package com.nival.chit.controllers;

import com.nival.chit.dto.GroupChatMessageDTO;
import com.nival.chit.services.GroupChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for private group chat per chit group.
 *
 * <p>Only members of the group should be allowed to use these endpoints;
 * role- and membership-based access control will be wired via security later.</p>
 */
@RestController
@RequestMapping("/api/chit/chat/")
@RequiredArgsConstructor
@Tag(name = "Group Chat", description = "Private chat area for each chit group. Only group members may participate.")
public class GroupChatController {

    private final GroupChatService groupChatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Send a chat message in a chit group's private chat.
     *
     * @param groupId id of the chit group
     * @param senderId id of the sending user
     * @param request message body wrapper
     * @return saved message DTO
     */
    @PostMapping("/group/{groupId}/send")
    @Operation(
            summary = "Send chat message",
            description = "Sends a chat message in a chit group's private chat. " +
                          "In the future, membership and role checks will ensure only group members can send messages."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = GroupChatMessageDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Chit group or user not found")
    })
    public ResponseEntity<GroupChatMessageDTO> sendMessage(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @RequestBody SendMessageRequest request
    ) {
        GroupChatMessageDTO message = groupChatService.sendMessage(groupId, request.content());
        
        // Broadcast the new message to active websocket subscribers in this group
        messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
        
        return ResponseEntity.ok(message);
    }

    /**
     * Get all messages for a chit group.
     *
     * @param groupId id of the chit group
     * @return list of message DTOs ordered by creation time
     */
    @GetMapping("/group/{groupId}/messages")
    @Operation(
            summary = "Get all messages for a group",
            description = "Retrieves the full message history for a chit group, ordered by creation time."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved messages",
                    content = @Content(schema = @Schema(implementation = GroupChatMessageDTO.class))
            )
    })
    public ResponseEntity<List<GroupChatMessageDTO>> getMessagesForGroup(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId
    ) {
        List<GroupChatMessageDTO> messages = groupChatService.getMessagesForGroup(groupId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get recent messages for a chit group after a specific message id.
     * Useful for polling in the client to fetch only new messages.
     *
     * @param groupId id of the chit group
     * @param lastMessageId last seen message id (optional)
     * @return list of new message DTOs
     */
    @GetMapping("/group/{groupId}/messages/recent")
    @Operation(
            summary = "Get recent messages",
            description = "Retrieves messages for a chit group created after a given message id. " +
                          "Useful for polling-based chat updates in the client."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved recent messages",
                    content = @Content(schema = @Schema(implementation = GroupChatMessageDTO.class))
            )
    })
    public ResponseEntity<List<GroupChatMessageDTO>> getRecentMessages(
            @Parameter(description = "ID of the chit group", required = true)
            @PathVariable Long groupId,
            @Parameter(description = "ID of the last seen message (optional)")
            @RequestParam(required = false) Long lastMessageId
    ) {
        List<GroupChatMessageDTO> messages = groupChatService.getRecentMessages(groupId, lastMessageId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Simple request body wrapper for sending a message.
     */
    public record SendMessageRequest(String content) { }
}

