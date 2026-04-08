package com.nival.chit.security;

import com.nival.chit.entity.User;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final String GROUP_TOPIC_PREFIX = "/topic/group/";

    private final AuthenticationManager authenticationManager;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            accessor.setUser(authenticate(accessor));
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        return message;
    }

    private Authentication authenticate(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic ")) {
            throw new AccessDeniedException("Missing websocket authorization");
        }

        String basicToken = authorization.substring(6).trim();
        String decoded = new String(Base64.getDecoder().decode(basicToken), StandardCharsets.UTF_8);
        int separatorIndex = decoded.indexOf(':');
        if (separatorIndex < 0) {
            throw new AccessDeniedException("Malformed websocket credentials");
        }

        String username = decoded.substring(0, separatorIndex);
        String password = decoded.substring(separatorIndex + 1);

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        );
        log.debug("Authenticated websocket session for {}", username);
        return authentication;
    }

    private void authorizeSubscription(StompHeaderAccessor accessor) {
        Authentication authentication = (Authentication) accessor.getUser();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required for websocket subscription");
        }

        String destination = accessor.getDestination();
        if (destination == null || !destination.startsWith(GROUP_TOPIC_PREFIX)) {
            return;
        }

        Long groupId = parseGroupId(destination);
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated websocket user not found"));

        if (accessControlService.isSaasAdmin(user)) {
            return;
        }

        membershipRepository.findByUserIdAndGroupIdAndStatus(user.getId(), groupId, UserStatus.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException("You are not allowed to subscribe to this group chat"));
    }

    private Long parseGroupId(String destination) {
        try {
            return Long.parseLong(destination.substring(GROUP_TOPIC_PREFIX.length()));
        } catch (NumberFormatException exception) {
            throw new AccessDeniedException("Invalid group chat destination");
        }
    }
}
