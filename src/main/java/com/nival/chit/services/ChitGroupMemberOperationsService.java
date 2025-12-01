package com.nival.chit.services;

import com.nival.chit.dto.MembershipDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Membership;
import com.nival.chit.entity.User;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.UserRepository;
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

        membership = membershipRepository.save(membership);
        log.info("User {} added to group {}", userId, groupId);

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
        List<Membership> memberships = membershipRepository.findByGroupId(groupId);
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
        Membership membership = membershipRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

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
                .joinDate(membership.getJoinDate())
                .status(membership.getStatus())
                .build();
    }
}
