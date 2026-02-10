package com.nival.chit.services;

import com.nival.chit.dto.AccountantAuditReportDTO;
import com.nival.chit.dto.GroupFinancialSummaryDTO;
import com.nival.chit.dto.MemberGroupFinancialSummaryDTO;
import com.nival.chit.dto.PaymentDTO;
import com.nival.chit.entity.*;
import com.nival.chit.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for real-time financial reporting at group and member level.
 *
 * <p>All calculations are done on demand as of \"today\" (daily granularity) using
 * current data from payments, loans, and funds.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ChitGroupRepository chitGroupRepository;
    private final FundsRepository fundsRepository;
    private final PaymentsRepository paymentsRepository;
    private final MemberLoanRepository memberLoanRepository;
    private final MembershipRepository membershipRepository;
    private final MemberLoanService memberLoanService;
    private final PaymentsService paymentsService;

    /**
     * Get an audit report for accountants showing unverified payments.
     */
    @Transactional(readOnly = true)
    public AccountantAuditReportDTO getAccountantAuditReport(Long groupId) {
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found"));

        int unverifiedCount = paymentsRepository.countUnverifiedByGroupId(groupId);
        Double unverifiedSum = paymentsRepository.sumUnverifiedAmountByGroupId(groupId);
        List<Payments> recentUnverified = paymentsRepository.findByChitGroupIdAndVerifiedFalseOrderByCreatedAtDesc(groupId);

        List<PaymentDTO> recentDTOs = recentUnverified.stream()
                .limit(10)
                .map(paymentsService::convertToDTO)
                .collect(Collectors.toList());

        return AccountantAuditReportDTO.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .unverifiedPaymentsCount(unverifiedCount)
                .totalUnverifiedAmount(unverifiedSum != null ? unverifiedSum : 0.0)
                .recentUnverifiedPayments(recentDTOs)
                .build();
    }

    /**
     * Get a real-time financial summary for a chit group as of today.
     *
     * @param groupId chit group id
     * @return group financial summary
     */
    @Transactional(readOnly = true)
    public GroupFinancialSummaryDTO getGroupSummary(Long groupId) {
        ChitGroup group = chitGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found"));

        LocalDate today = LocalDate.now();
        LocalDateTime endOfToday = today.atTime(23, 59, 59);

        // Total contributions collected so far for this group
        double totalContributions = paymentsRepository.sumCompletedByGroupUpTo(groupId, endOfToday);

        // Total loans outstanding (principal + interest up to today)
        List<MemberLoan> groupLoans = memberLoanRepository.findByChitGroupId(groupId);
        double totalLoansOutstanding = groupLoans.stream()
                .mapToDouble(memberLoanService::calculateCurrentPayable)
                .sum();

        // Approximate fund balance: use Funds.totalAmount if present,
        // otherwise approximate as totalContributions - totalLoansOutstanding
        double fundBalanceApprox = fundsRepository.findAll().stream()
                .filter(f -> f.getChitGroup().getId() == groupId)
                .mapToDouble(Funds::getTotalAmount)
                .findFirst()
                .orElse(totalContributions - totalLoansOutstanding);

        int memberCount = group.getMemberships() != null ? group.getMemberships().size() : 0;

        return GroupFinancialSummaryDTO.builder()
                .chitGroupId(group.getId())
                .chitGroupName(group.getName())
                .chitGroupCode(group.getGroupCode())
                .totalMembers(memberCount)
                .monthlyContributionAmount(group.getMonthlyAmount())
                .totalContributionsCollected(totalContributions)
                .totalLoansOutstanding(totalLoansOutstanding)
                .fundBalanceApprox(fundBalanceApprox)
                .asOfDate(today)
                .build();
    }

    /**
     * Get financial summaries for a member across all chit groups where they are a member.
     *
     * @param userId user id
     * @return list of financial summaries per chit group
     */
    @Transactional(readOnly = true)
    public List<MemberGroupFinancialSummaryDTO> getMemberGroupSummaries(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime endOfToday = today.atTime(23, 59, 59);

        List<Membership> memberships = membershipRepository.findByUserId(userId);

        return memberships.stream()
                .map(membership -> {
                    ChitGroup group = membership.getChitGroup();

                    double contributions = paymentsRepository
                            .sumUserCompletedByGroupUpTo(group.getId(), userId, endOfToday);

                    // Current loan payable in this group for the user (if any)
                    Double currentLoanPayable = memberLoanRepository
                            .findByUserIdAndChitGroupId(userId, group.getId())
                            .map(memberLoanService::calculateCurrentPayable)
                            .orElse(null);

                    // Approximate profit share: take group's approximate fund balance
                    // minus total contributions, evenly divided among members.
                    GroupFinancialSummaryDTO groupSummary = getGroupSummary(group.getId());
                    double profitPool = groupSummary.getFundBalanceApprox()
                            - groupSummary.getTotalContributionsCollected()
                            + groupSummary.getTotalLoansOutstanding();

                    Double profitShare = null;
                    if (groupSummary.getTotalMembers() > 0) {
                        profitShare = profitPool / groupSummary.getTotalMembers();
                    }

                    return MemberGroupFinancialSummaryDTO.builder()
                            .userId(userId)
                            .userName(membership.getUser().getName())
                            .chitGroupId(group.getId())
                            .chitGroupName(group.getName())
                            .chitGroupCode(group.getGroupCode())
                            .totalContributionsPaid(contributions)
                            .currentLoanPayable(currentLoanPayable)
                            .estimatedProfitShare(profitShare)
                            .asOfDate(today)
                            .build();
                })
                .collect(Collectors.toList());
    }
}


