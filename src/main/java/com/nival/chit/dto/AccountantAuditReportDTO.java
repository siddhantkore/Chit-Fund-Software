package com.nival.chit.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountantAuditReportDTO {
    private Long groupId;
    private String groupName;
    private int unverifiedPaymentsCount;
    private double totalUnverifiedAmount;
    private List<PaymentDTO> recentUnverifiedPayments;
}
