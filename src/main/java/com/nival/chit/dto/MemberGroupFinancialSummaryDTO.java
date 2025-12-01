package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Summary of a member's financial position within a specific chit group.
 *
 * <p>Includes:</p>
 * <ul>
 *     <li>Total contributions paid to the group</li>
 *     <li>Current loan payable (if any) in the group</li>
 *     <li>Approximate profit share from group interest (evenly divided among members)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGroupFinancialSummaryDTO {

    private Long userId;
    private String userName;

    private Long chitGroupId;
    private String chitGroupName;
    private String chitGroupCode;

    private double totalContributionsPaid;
    private Double currentLoanPayable;
    private Double estimatedProfitShare;

    private LocalDate asOfDate;
}


