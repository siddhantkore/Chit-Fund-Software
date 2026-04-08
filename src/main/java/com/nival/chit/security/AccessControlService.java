package com.nival.chit.security;

import com.nival.chit.entity.Membership;
import com.nival.chit.entity.User;
import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centralized access checks driven by the authenticated user.
 */
@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentication required");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user no longer exists"));
    }

    @Transactional(readOnly = true)
    public boolean isSaasAdmin() {
        return isSaasAdmin(getCurrentUser());
    }

    public boolean isSaasAdmin(User user) {
        return user.getRole() == UserRoles.ADMIN;
    }

    @Transactional(readOnly = true)
    public void requireSelfOrSaasAdmin(Long userId) {
        User currentUser = getCurrentUser();
        if (isSaasAdmin(currentUser) || currentUser.getId() == userId) {
            return;
        }
        throw new AccessDeniedException("You do not have access to this user scope");
    }

    @Transactional(readOnly = true)
    public void requireSaasAdmin() {
        if (!isSaasAdmin()) {
            throw new AccessDeniedException("SaaS admin access is required");
        }
    }

    @Transactional(readOnly = true)
    public Membership requireActiveGroupMembership(Long groupId) {
        User currentUser = getCurrentUser();
        if (isSaasAdmin(currentUser)) {
            return null;
        }

        return membershipRepository.findByUserIdAndGroupIdAndStatus(currentUser.getId(), groupId, UserStatus.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException("You are not an active member of this group"));
    }

    @Transactional(readOnly = true)
    public Membership requireGroupAdmin(Long groupId) {
        User currentUser = getCurrentUser();
        if (isSaasAdmin(currentUser)) {
            return null;
        }

        Membership membership = membershipRepository.findByUserIdAndGroupIdAndStatus(currentUser.getId(), groupId, UserStatus.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException("You are not an active member of this group"));

        if (membership.getRole() != GroupRole.ADMIN) {
            throw new AccessDeniedException("Group admin access is required");
        }

        return membership;
    }

    @Transactional(readOnly = true)
    public void requireSelfOrGroupAdmin(Long userId, Long groupId) {
        User currentUser = getCurrentUser();
        if (isSaasAdmin(currentUser)) {
            return;
        }

        if (currentUser.getId() == userId) {
            requireActiveGroupMembership(groupId);
            return;
        }

        requireGroupAdmin(groupId);
    }
}
