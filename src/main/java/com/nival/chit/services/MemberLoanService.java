package com.nival.chit.services;

import com.nival.chit.dto.MemberLoanResponseDTO;
import com.nival.chit.dto.MemberLoanSummaryDTO;
import com.nival.chit.entity.MemberLoan;
import com.nival.chit.repository.MemberLoanRepository;
import com.nival.chit.security.AccessControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing member loans.
 * Handles loan calculations, retrieval, and daily interest updates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLoanService {

    private final MemberLoanRepository memberLoanRepository;
    private final AccessControlService accessControlService;

    /**
     * Calculate the current payable amount for a loan based on daily interest.
     * Interest is calculated daily from the start date using the annual interest rate.
     * Formula: currentPayable = principle + (principle * interestRate * daysElapsed / 36500)
     *
     * @param loan the member loan entity
     * @return the updated current payable amount
     */
    @Transactional(readOnly = true)
    public double calculateCurrentPayable(MemberLoan loan) {
        if (loan.getStatus().equals("CLOSED") || loan.getStatus().equals("PAID")) {
            return loan.getCurrentPayable();
        }

        LocalDateTime startDate = loan.getStartDate();
        LocalDateTime currentDate = LocalDateTime.now();
        
        // Calculate days elapsed since loan start
        long daysElapsed = ChronoUnit.DAYS.between(startDate.toLocalDate(), currentDate.toLocalDate());
        
        if (daysElapsed <= 0) {
            return loan.getPrinciple();
        }

        // Calculate daily interest: (principle * annualInterestRate * days) / 36500
        // Assuming interest field stores annual percentage rate (e.g., 12 for 12%)
        double annualInterestRate = loan.getInterest();
        double interestAmount = (loan.getPrinciple() * annualInterestRate * daysElapsed) / 36500.0;
        
        // Current payable = principle + accumulated interest
        double currentPayable = loan.getPrinciple() + interestAmount;
        
        log.debug("Loan ID {}: Days elapsed: {}, Interest: {}, Current Payable: {}", 
                loan.getId(), daysElapsed, interestAmount, currentPayable);
        
        return currentPayable;
    }

    /**
     * Get all loans for members in a specific chit group.
     * Used by group admins to view all loans in their group.
     *
     * @param chitGroupId the ID of the chit group
     * @return list of loan response DTOs with current payable amounts
     */
    @Transactional(readOnly = true)
    public List<MemberLoanResponseDTO> getAllLoansByGroupId(Long chitGroupId) {
        accessControlService.requireGroupAdmin(chitGroupId);
        List<MemberLoan> loans = memberLoanRepository.findByChitGroupId(chitGroupId);
        
        return loans.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all loans for a specific user across all groups.
     * Used by members to view their complete loan portfolio.
     *
     * @param userId the ID of the user
     * @return list of loan summary DTOs with current payable amounts
     */
    @Transactional(readOnly = true)
    public List<MemberLoanSummaryDTO> getAllLoansByUserId(Long userId) {
        accessControlService.requireSelfOrSaasAdmin(userId);
        List<MemberLoan> loans = memberLoanRepository.findByUserId(userId);
        
        return loans.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get current payable amount for a user's loan in a specific group.
     * Returns real-time calculation of principle + interest.
     *
     * @param userId the ID of the user
     * @param chitGroupId the ID of the chit group
     * @return the current payable amount, or null if loan not found
     */
    @Transactional(readOnly = true)
    public Double getCurrentPayable(Long userId, Long chitGroupId) {
        accessControlService.requireSelfOrGroupAdmin(userId, chitGroupId);
        return memberLoanRepository.findByUserIdAndChitGroupId(userId, chitGroupId)
                .map(this::calculateCurrentPayable)
                .orElse(null);
    }

    /**
     * Get detailed loan information for a user in a specific group.
     *
     * @param userId the ID of the user
     * @param chitGroupId the ID of the chit group
     * @return loan response DTO with current payable, or null if not found
     */
    @Transactional(readOnly = true)
    public MemberLoanResponseDTO getLoanByUserAndGroup(Long userId, Long chitGroupId) {
        accessControlService.requireSelfOrGroupAdmin(userId, chitGroupId);
        return memberLoanRepository.findByUserIdAndChitGroupId(userId, chitGroupId)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    /**
     * Convert MemberLoan entity to MemberLoanResponseDTO with current payable calculation.
     *
     * @param loan the member loan entity
     * @return response DTO with updated current payable
     */
    private MemberLoanResponseDTO convertToResponseDTO(MemberLoan loan) {
        double currentPayable = calculateCurrentPayable(loan);
        
        return MemberLoanResponseDTO.builder()
                .id(loan.getId())
                .principle(loan.getPrinciple())
                .interestRate(loan.getInterest())
                .tenure(loan.getTenure())
                .currentPayable(currentPayable)
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate())
                .status(loan.getStatus())
                .chitGroupId(loan.getChitGroup().getId())
                .chitGroupName(loan.getChitGroup().getName())
                .chitGroupCode(loan.getChitGroup().getGroupCode())
                .userId(loan.getUser().getId())
                .userName(loan.getUser().getName())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }

    /**
     * Convert MemberLoan entity to MemberLoanSummaryDTO with current payable calculation.
     *
     * @param loan the member loan entity
     * @return summary DTO with updated current payable and days remaining
     */
    private MemberLoanSummaryDTO convertToSummaryDTO(MemberLoan loan) {
        double currentPayable = calculateCurrentPayable(loan);
        
        Long daysRemaining = null;
        if (loan.getEndDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(), loan.getEndDate().toLocalDate());
            daysRemaining = days > 0 ? days : 0L;
        }
        
        return MemberLoanSummaryDTO.builder()
                .loanId(loan.getId())
                .chitGroupId(loan.getChitGroup().getId())
                .chitGroupName(loan.getChitGroup().getName())
                .chitGroupCode(loan.getChitGroup().getGroupCode())
                .principle(loan.getPrinciple())
                .currentPayable(currentPayable)
                .status(loan.getStatus())
                .daysRemaining(daysRemaining)
                .build();
    }
}
