package com.nival.chit.services;

import com.nival.chit.dto.CreatePaymentDTO;
import com.nival.chit.dto.PaymentDTO;
import com.nival.chit.entity.ChitGroup;
import com.nival.chit.entity.Membership;
import com.nival.chit.entity.Payments;
import com.nival.chit.entity.User;
import com.nival.chit.enums.GroupRole;
import com.nival.chit.enums.UserRoles;
import com.nival.chit.enums.UserStatus;
import com.nival.chit.repository.ChitGroupRepository;
import com.nival.chit.repository.MembershipRepository;
import com.nival.chit.repository.PaymentsRepository;
import com.nival.chit.repository.UserRepository;
import com.nival.chit.security.AccessControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing payments.
 * Handles recording monthly contributions and loan repayments.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final UserRepository userRepository;
    private final ChitGroupRepository chitGroupRepository;
    private final MembershipRepository membershipRepository;
    private final AccessControlService accessControlService;

    /**
     * Record a payment (monthly contribution or loan repayment).
     *
     * @param createDTO the payment data
     * @return the created payment DTO
     * @throws IllegalArgumentException if user or group not found
     */
    @Transactional
    public PaymentDTO createPayment(CreatePaymentDTO createDTO) {
        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + createDTO.getUserId()));

        ChitGroup group = chitGroupRepository.findById(createDTO.getChitGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Chit group not found: " + createDTO.getChitGroupId()));

        Membership membership = membershipRepository
                .findByUserIdAndGroupIdAndStatus(user.getId(), group.getId(), UserStatus.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException("Payments can only be recorded for active group members"));

        User currentUser = accessControlService.getCurrentUser();
        boolean canRecordForAnotherMember = accessControlService.isSaasAdmin(currentUser)
                || currentUser.getRole() == UserRoles.ACCOUNTANT
                || membershipRepository.findByUserIdAndGroupIdAndStatus(currentUser.getId(), group.getId(), UserStatus.ACTIVE)
                .map(currentMembership -> currentMembership.getRole() == GroupRole.ADMIN)
                .orElse(false);

        if (currentUser.getId() != user.getId() && !canRecordForAnotherMember) {
            throw new AccessDeniedException("You can only record payments for yourself unless you administer this group");
        }

        Payments payment = new Payments();
        payment.setMonth(createDTO.getMonth());
        payment.setAmountPaid(createDTO.getAmountPaid());
        payment.setPaymentDate(createDTO.getPaymentDate() != null ? createDTO.getPaymentDate() : LocalDateTime.now());
        payment.setMode(createDTO.getMode());
        payment.setStatus("COMPLETED");
        payment.setUser(user);
        payment.setChitGroup(group);

        payment = paymentsRepository.save(payment);
        log.info("Payment recorded: {} for user {} in group {}", createDTO.getAmountPaid(), createDTO.getUserId(), createDTO.getChitGroupId());

        return convertToDTO(payment);
    }

    /**
     * Get all payments for a user in a specific group.
     *
     * @param userId the user ID
     * @param groupId the chit group ID
     * @return list of payment DTOs
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByUserAndGroup(Long userId, Long groupId) {
        accessControlService.requireSelfOrGroupAdmin(userId, groupId);
        List<Payments> payments = paymentsRepository.findByGroupAndUser(groupId, userId);
        return payments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all payments for a chit group.
     *
     * @param groupId the chit group ID
     * @return list of payment DTOs
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByGroup(Long groupId) {
        accessControlService.requireGroupAdmin(groupId);
        return paymentsRepository.findAll().stream()
                .filter(p -> p.getChitGroup().getId() == groupId)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update payment status.
     *
     * @param paymentId the payment ID
     * @param status the new status
     * @return updated payment DTO
     * @throws IllegalArgumentException if payment not found
     */
    @Transactional
    public PaymentDTO updatePaymentStatus(Long paymentId, String status) {
        Payments payment = paymentsRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        accessControlService.requireGroupAdmin(payment.getChitGroup().getId());

        payment.setStatus(status);
        payment = paymentsRepository.save(payment);
        log.info("Payment status updated: {} to {}", paymentId, status);

        return convertToDTO(payment);
    }

    @Transactional
    public PaymentDTO verifyPayment(Long paymentId, Long accountantId) {
        Payments payment = paymentsRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        User verifier = accessControlService.getCurrentUser();

        boolean canVerify = accessControlService.isSaasAdmin(verifier)
                || verifier.getRole() == UserRoles.ACCOUNTANT
                || membershipRepository.findByUserIdAndGroupIdAndStatus(
                        verifier.getId(),
                        payment.getChitGroup().getId(),
                        UserStatus.ACTIVE
                ).map(membership -> membership.getRole() == GroupRole.ADMIN)
                .orElse(false);
        if (!canVerify) {
            throw new AccessDeniedException("Only a group admin, accountant, or SaaS admin can verify payments");
        }

        payment.setVerified(true);
        payment.setVerifiedBy(verifier);
        payment.setVerifiedAt(LocalDateTime.now());
        payment.setStatus("COMPLETED"); // Auto-complete on verification if needed

        payment = paymentsRepository.save(payment);
        log.info("Payment {} verified by user {} (requested accountantId={})", paymentId, verifier.getId(), accountantId);

        return convertToDTO(payment);
    }

    /**
     * Convert Payments entity to PaymentDTO.
     */
    public PaymentDTO convertToDTO(Payments payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .month(payment.getMonth())
                .amountPaid(payment.getAmountPaid())
                .paymentDate(payment.getPaymentDate())
                .mode(payment.getMode())
                .status(payment.getStatus())
                .userId(payment.getUser().getId())
                .userName(payment.getUser().getName())
                .chitGroupId(payment.getChitGroup().getId())
                .chitGroupName(payment.getChitGroup().getName())
                .verified(payment.isVerified())
                .verifiedByUserId(payment.getVerifiedBy() != null ? payment.getVerifiedBy().getId() : null)
                .verifiedByUserName(payment.getVerifiedBy() != null ? payment.getVerifiedBy().getName() : null)
                .verifiedAt(payment.getVerifiedAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
