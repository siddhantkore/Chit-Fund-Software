package com.nival.chit.services;

import com.nival.chit.dto.FundsDTO;
import com.nival.chit.entity.Funds;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.FundsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing funds.
 * Handles fund operations and balance tracking for chit groups.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FundsService {

    private final FundsRepository fundsRepository;
    private final ChitGroupRepository chitGroupRepository;

    /**
     * Get fund by chit group ID.
     *
     * @param groupId the chit group ID
     * @return fund DTO or null if not found
     */
    @Transactional(readOnly = true)
    public FundsDTO getFundByGroupId(Long groupId) {
        return fundsRepository.findAll().stream()
                .filter(f -> f.getChitGroup().getId() == groupId)
                .findFirst()
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Get fund by ID.
     *
     * @param fundId the fund ID
     * @return fund DTO or null if not found
     */
    @Transactional(readOnly = true)
    public FundsDTO getFundById(Long fundId) {
        return fundsRepository.findById(fundId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Get all funds.
     *
     * @return list of all fund DTOs
     */
    @Transactional(readOnly = true)
    public List<FundsDTO> getAllFunds() {
        return fundsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update fund total amount.
     *
     * @param fundId the fund ID
     * @param amount the new total amount
     * @return updated fund DTO
     * @throws IllegalArgumentException if fund not found
     */
    @Transactional
    public FundsDTO updateFundAmount(Long fundId, Double amount) {
        Funds fund = fundsRepository.findById(fundId)
                .orElseThrow(() -> new IllegalArgumentException("Fund not found: " + fundId));

        fund.setTotalAmount(amount);
        fund = fundsRepository.save(fund);
        log.info("Fund amount updated: {} to {}", fundId, amount);

        return convertToDTO(fund);
    }

    /**
     * Update fund interest rate.
     *
     * @param fundId the fund ID
     * @param interestRate the new interest rate
     * @return updated fund DTO
     * @throws IllegalArgumentException if fund not found
     */
    @Transactional
    public FundsDTO updateFundInterestRate(Long fundId, Double interestRate) {
        Funds fund = fundsRepository.findById(fundId)
                .orElseThrow(() -> new IllegalArgumentException("Fund not found: " + fundId));

        fund.setInterestRate(interestRate);
        fund = fundsRepository.save(fund);
        log.info("Fund interest rate updated: {} to {}", fundId, interestRate);

        return convertToDTO(fund);
    }

    /**
     * Convert Funds entity to FundsDTO.
     */
    private FundsDTO convertToDTO(Funds fund) {
        return FundsDTO.builder()
                .id(fund.getId())
                .name(fund.getName())
                .totalAmount(fund.getTotalAmount())
                .cyclePeriod(fund.getCyclePeriod())
                .interestRate(fund.getInterestRate())
                .chitGroupId(fund.getChitGroup().getId())
                .chitGroupName(fund.getChitGroup().getName())
                .createdAt(fund.getCreatedAt())
                .updatedAt(fund.getUpdatedAt())
                .build();
    }
}

