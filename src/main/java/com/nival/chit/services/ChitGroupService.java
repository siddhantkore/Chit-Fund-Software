package com.nival.chit.services;

import com.nival.chit.dto.ChitGroupDTO;
import com.nival.chit.dto.CreateChitGroupDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Funds;
import com.nival.chit.entity.Membership;
import com.nival.chit.entity.User;
import com.nival.chit.enums.ChitGroupStatus;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.FundsRepository;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing chit groups.
 * Handles creation, updates, deletion, and search operations for chit groups.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChitGroupService {

    private final ChitGroupRepository chitGroupRepository;
    private final FundsRepository fundsRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    /**
     * Create a new chit group with associated fund.
     *
     * @param createDTO the chit group creation data
     * @param creatorUserId the user ID of the creator (who will be group admin)
     * @return the created chit group DTO
     */
    @Transactional
    public ChitGroupDTO createChitGroup(CreateChitGroupDTO createDTO, Long creatorUserId) {
        ChitGroup group = new ChitGroup();
        group.setName(createDTO.getName());
        group.setDuration(createDTO.getDuration());
        group.setMonthlyAmount(createDTO.getMonthlyAmount());
        group.setTotalMembers(createDTO.getTotalMembers());
        group.setStartDate(createDTO.getStartDate() != null ? createDTO.getStartDate() : LocalDateTime.now());
        group.setStatus(ChitGroupStatus.PENDING);

        // Group code is auto-generated in @PrePersist
        group = chitGroupRepository.save(group);

        // Create associated fund
        Funds fund = new Funds();
        fund.setName("Fund for " + group.getName());
        fund.setTotalAmount(0.0);
        fund.setCyclePeriod(createDTO.getDuration());
        fund.setInterestRate(0.0); // Will be set based on group rules
        fund.setChitGroup(group);
        fund = fundsRepository.save(fund);

        group.setFundId(fund);
        group = chitGroupRepository.save(group);

        // Auto-add creator as Admin of the group
        if (creatorUserId != null) {
            User creator = userRepository.findById(creatorUserId)
                    .orElseThrow(() -> new IllegalArgumentException("Creator user not found: " + creatorUserId));
            
            Membership adminMembership = new Membership();
            adminMembership.setUser(creator);
            adminMembership.setChitGroup(group);
            adminMembership.setRole(UserRoles.ADMIN);
            adminMembership.setStatus(UserStatus.ACTIVE);
            membershipRepository.save(adminMembership);
            log.info("Auto-added creator {} as Admin for group {}", creatorUserId, group.getName());
        }

        log.info("Chit group created: {} with code: {}", group.getName(), group.getGroupCode());
        return convertToDTO(group);
    }

    /**
     * Get chit group by ID.
     *
     * @param groupId the chit group ID
     * @return chit group DTO or null if not found
     */
    @Transactional(readOnly = true)
    public ChitGroupDTO getChitGroupById(Long groupId) {
        return chitGroupRepository.findById(groupId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Get chit group by group code.
     *
     * @param groupCode the unique group code
     * @return chit group DTO or null if not found
     */
    @Transactional(readOnly = true)
    public ChitGroupDTO getChitGroupByCode(String groupCode) {
        return chitGroupRepository.findByGroupCode(groupCode)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Search chit groups by name (case-insensitive partial match).
     *
     * @param name the name to search
     * @return list of matching chit group DTOs
     */
    @Transactional(readOnly = true)
    public List<ChitGroupDTO> searchChitGroupsByName(String name) {
        List<ChitGroup> groups = chitGroupRepository.findByNameContainingIgnoreCase(name);
        return groups.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all chit groups.
     *
     * @return list of all chit group DTOs
     */
    @Transactional(readOnly = true)
    public List<ChitGroupDTO> getAllChitGroups() {
        return chitGroupRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get chit groups by status.
     *
     * @param status the group status
     * @return list of chit group DTOs with the specified status
     */
    @Transactional(readOnly = true)
    public List<ChitGroupDTO> getChitGroupsByStatus(ChitGroupStatus status) {
        List<ChitGroup> groups = chitGroupRepository.findByStatus(status);
        return groups.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update chit group information.
     *
     * @param groupId the chit group ID
     * @param updateDTO the updated chit group data
     * @return updated chit group DTO
     * @throws IllegalArgumentException if group not found
     */
    @Transactional
    public ChitGroupDTO updateChitGroup(Long groupId, CreateChitGroupDTO updateDTO) {
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> {
                    log.warn("Attempted to update non-existent ChitGroup: {}", groupId);
                    return new IllegalArgumentException("Chit group not found: " + groupId);
                });

        if (updateDTO.getName() != null) {
            group.setName(updateDTO.getName());
        }
        if (updateDTO.getDuration() != null) {
            group.setDuration(updateDTO.getDuration());
        }
        if (updateDTO.getMonthlyAmount() != null) {
            group.setMonthlyAmount(updateDTO.getMonthlyAmount());
        }
        if (updateDTO.getTotalMembers() != null) {
            group.setTotalMembers(updateDTO.getTotalMembers());
        }
        if (updateDTO.getStartDate() != null) {
            group.setStartDate(updateDTO.getStartDate());
        }

        group = chitGroupRepository.save(group);
        log.info("Chit group updated: {}", group.getName());

        return convertToDTO(group);
    }

    /**
     * Update chit group status.
     *
     * @param groupId the chit group ID
     * @param status the new status
     * @return updated chit group DTO
     * @throws IllegalArgumentException if group not found
     */
    @Transactional
    public ChitGroupDTO updateChitGroupStatus(Long groupId, ChitGroupStatus status) {
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> {
                    log.warn("Attempted to update status of non-existent ChitGroup: {}", groupId);
                    return new IllegalArgumentException("Chit group not found: " + groupId);
                });

        group.setStatus(status);
        group = chitGroupRepository.save(group);
        log.info("Chit group status updated: {} to {}", group.getName(), status);

        return convertToDTO(group);
    }

    /**
     * Delete a chit group.
     * This will cascade delete related records (memberships, loans, payments, etc.).
     *
     * @param groupId the chit group ID
     * @throws IllegalArgumentException if group not found
     */
    @Transactional
    public void deleteChitGroup(Long groupId) {
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete non-existent ChitGroup: {}", groupId);
                    return new IllegalArgumentException("Chit group not found: " + groupId);
                });

        chitGroupRepository.delete(group);
        log.info("Chit group deleted: {}", group.getName());
    }

    /**
     * Convert ChitGroup entity to ChitGroupDTO.
     */
    private ChitGroupDTO convertToDTO(ChitGroup group) {
        return ChitGroupDTO.builder()
                .id(group.getId())
                .groupCode(group.getGroupCode())
                .name(group.getName())
                .duration(group.getDuration())
                .monthlyAmount(group.getMonthlyAmount())
                .totalMembers(group.getTotalMembers())
                .startDate(group.getStartDate())
                .status(group.getStatus())
                .fundId(group.getFundId() != null ? group.getFundId().getId() : null)
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }
}

