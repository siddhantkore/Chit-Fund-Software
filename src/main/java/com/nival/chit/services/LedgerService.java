package com.nival.chit.services;

import com.nival.chit.dto.LedgerDTO;
import com.nival.chit.entity.Ledger;
import com.nival.chit.repository.LedgerRepository;
import com.nival.chit.security.AccessControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing ledger entries.
 * Handles ledger entry retrieval and viewing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final AccessControlService accessControlService;

    /**
     * Get all ledger entries.
     *
     * @return list of all ledger DTOs
     */
    @Transactional(readOnly = true)
    public List<LedgerDTO> getAllLedgerRecords() {
        accessControlService.requireSaasAdmin();
        return ledgerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all ledger entries for a chit group.
     *
     * @param groupId the chit group ID
     * @return list of ledger DTOs
     */
    @Transactional(readOnly = true)
    public List<LedgerDTO> getLedgerByGroup(Long groupId) {
        accessControlService.requireGroupAdmin(groupId);
        List<Ledger> entries = ledgerRepository.findByChitGroupId(groupId);
        return entries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all ledger entries for a user.
     *
     * @param userId the user ID
     * @return list of ledger DTOs
     */
    @Transactional(readOnly = true)
    public List<LedgerDTO> getLedgerByUser(Long userId) {
        accessControlService.requireSelfOrSaasAdmin(userId);
        List<Ledger> entries = ledgerRepository.findByUserId(userId);
        return entries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all ledger entries for a user in a specific group.
     *
     * @param userId the user ID
     * @param groupId the chit group ID
     * @return list of ledger DTOs
     */
    @Transactional(readOnly = true)
    public List<LedgerDTO> getLedgerByUserAndGroup(Long userId, Long groupId) {
        accessControlService.requireSelfOrGroupAdmin(userId, groupId);
        List<Ledger> entries = ledgerRepository.findByUserIdAndChitGroupId(userId, groupId);
        return entries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get ledger entry by ID.
     *
     * @param ledgerId the ledger entry ID
     * @return ledger DTO or null if not found
     */
    @Transactional(readOnly = true)
    public LedgerDTO getLedgerById(Long ledgerId) {
        return ledgerRepository.findById(ledgerId)
                .map(ledger -> {
                    accessControlService.requireSelfOrGroupAdmin(ledger.getUser().getId(), ledger.getChitGroup().getId());
                    return convertToDTO(ledger);
                })
                .orElse(null);
    }

    /**
     * Convert Ledger entity to LedgerDTO.
     */
    private LedgerDTO convertToDTO(Ledger ledger) {
        return LedgerDTO.builder()
                .id(ledger.getId())
                .entryType(ledger.getEntryType())
                .amount(ledger.getAmount())
                .entryDate(ledger.getEntryDate())
                .remark(ledger.getRemark())
                .referenceId(ledger.getReferenceId())
                .userId(ledger.getUser().getId())
                .userName(ledger.getUser().getName())
                .chitGroupId(ledger.getChitGroup().getId())
                .chitGroupName(ledger.getChitGroup().getName())
                .createdAt(ledger.getCreatedAt())
                .updatedAt(ledger.getUpdatedAt())
                .build();
    }
}
