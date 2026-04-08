package com.nival.chit.services;

import com.nival.chit.dto.MembershipDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Membership;
import com.nival.chit.entity.User;
import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.UserRepository;
import com.nival.chit.security.AccessControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing chit group member operations.
 * Handles adding and removing members from chit groups.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChitGroupMemberOperationsService {

    private final MembershipRepository membershipRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AccessControlService accessControlService;

    /**
     * Add a member to a chit group.
     *
     * @param groupId the chit group ID
     * @param userId the user ID to add
     * @return the created membership DTO
     * @throws IllegalArgumentException if group/user not found or user already a member
     */
    @Transactional
    public MembershipDTO addMemberToGroup(Long groupId, Long userId) {
        accessControlService.requireGroupAdmin(groupId);
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Check if user is already a member
        if (membershipRepository.findByUserIdAndGroupId(userId, groupId).isPresent()) {
            throw new IllegalArgumentException("User is already a member of this group");
        }

        // Check if group has reached maximum members
        long currentMemberCount = membershipRepository.findByGroupId(groupId).size();
        if (currentMemberCount >= group.getTotalMembers()) {
            throw new IllegalArgumentException("Chit group has reached maximum member limit");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setChitGroup(group);
        membership.setStatus(UserStatus.ACTIVE);
        membership.setRole(GroupRole.MEMBER);

        membership = membershipRepository.save(membership);
        log.info("User {} added to group {} as MEMBER", userId, groupId);

        return convertToDTO(membership);
    }

    /**
     * Join a group using a group code.
     *
     * @param groupCode the unique group code
     * @param userId the user ID who wants to join
     * @return the created membership DTO
     */
    @Transactional
    public MembershipDTO joinGroupByCode(String groupCode) {
        User currentUser = accessControlService.getCurrentUser();
        ChitGroup group = chitGroupRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group code: " + groupCode));
        Long userId = currentUser.getId();

        Membership existingMembership = membershipRepository.findByUserIdAndGroupId(userId, group.getId()).orElse(null);
        if (existingMembership != null) {
            if (existingMembership.getStatus() == UserStatus.PENDING) {
                throw new IllegalArgumentException("A join request for this group is already pending");
            }
            throw new IllegalArgumentException("User is already associated with this group");
        }

        long activeMemberCount = membershipRepository.findByGroupIdAndStatus(group.getId(), UserStatus.ACTIVE).size();
        if (activeMemberCount >= group.getTotalMembers()) {
            throw new IllegalArgumentException("Chit group has reached maximum member limit");
        }

        Membership membership = new Membership();
        membership.setUser(currentUser);
        membership.setChitGroup(group);
        membership.setStatus(UserStatus.PENDING);
        membership.setRole(GroupRole.MEMBER);
        membership = membershipRepository.save(membership);

        membershipRepository.findByGroupIdAndRoleAndStatus(group.getId(), GroupRole.ADMIN, UserStatus.ACTIVE)
                .forEach(adminMembership -> notificationService.createNotification(
                        adminMembership.getUser().getId(),
                        "Join request from " + currentUser.getName() + " for group " + group.getName(),
                        "JOIN_REQUEST"
                ));

        log.info("Join request created for user {} in group {}", userId, group.getId());
        return convertToDTO(membership);
    }

    /**
     * Remove a member from a chit group.
     *
     * @param groupId the chit group ID
     * @param userId the user ID to remove
     * @throws IllegalArgumentException if membership not found
     */
    @Transactional
    public void removeMemberFromGroup(Long groupId, Long userId) {
        accessControlService.requireGroupAdmin(groupId);
        Membership membership = membershipRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

        membershipRepository.delete(membership);
        log.info("User {} removed from group {}", userId, groupId);
    }

    /**
     * Get all members of a chit group.
     *
     * @param groupId the chit group ID
     * @return list of membership DTOs
     */
    @Transactional(readOnly = true)
    public List<MembershipDTO> getGroupMembers(Long groupId) {
        Membership viewerMembership = accessControlService.requireActiveGroupMembership(groupId);
        List<Membership> memberships = membershipRepository.findByGroupId(groupId);
        if (viewerMembership != null && viewerMembership.getRole() != GroupRole.ADMIN) {
            memberships = memberships.stream()
                    .filter(membership -> membership.getStatus() == UserStatus.ACTIVE)
                    .toList();
        }
        return memberships.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all groups a user is a member of.
     *
     * @param userId the user ID
     * @return list of membership DTOs
     */
    @Transactional(readOnly = true)
    public List<MembershipDTO> getUserMemberships(Long userId) {
        accessControlService.requireSelfOrSaasAdmin(userId);
        List<Membership> memberships = membershipRepository.findByUserId(userId);
        return memberships.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update membership status.
     *
     * @param groupId the chit group ID
     * @param userId the user ID
     * @param status the new status
     * @return updated membership DTO
     * @throws IllegalArgumentException if membership not found
     */
    @Transactional
    public MembershipDTO updateMembershipStatus(Long groupId, Long userId, UserStatus status) {
        accessControlService.requireGroupAdmin(groupId);
        Membership membership = membershipRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

        if (status == UserStatus.ACTIVE && membership.getStatus() != UserStatus.ACTIVE) {
            ChitGroup group = membership.getChitGroup();
            long activeMemberCount = membershipRepository.findByGroupIdAndStatus(groupId, UserStatus.ACTIVE).size();
            if (activeMemberCount >= group.getTotalMembers()) {
                throw new IllegalArgumentException("Chit group has reached maximum member limit");
            }
        }

        membership.setStatus(status);
        membership = membershipRepository.save(membership);
        log.info("Membership status updated for user {} in group {} to {}", userId, groupId, status);

        return convertToDTO(membership);
    }

    /**
     * Convert Membership entity to MembershipDTO.
     */
    private MembershipDTO convertToDTO(Membership membership) {
        return MembershipDTO.builder()
                .id(membership.getId())
                .userId(membership.getUser().getId())
                .userName(membership.getUser().getName())
                .chitGroupId(membership.getChitGroup().getId())
                .chitGroupName(membership.getChitGroup().getName())
                .chitGroupCode(membership.getChitGroup().getGroupCode())
                .monthlyAmount(membership.getChitGroup().getMonthlyAmount())
                .duration(membership.getChitGroup().getDuration())
                .totalMembers(membership.getChitGroup().getTotalMembers())
                .startDate(membership.getChitGroup().getStartDate())
                .joinDate(membership.getJoinDate())
                .status(membership.getStatus())
                .role(membership.getRole())
                .build();
    }
}
