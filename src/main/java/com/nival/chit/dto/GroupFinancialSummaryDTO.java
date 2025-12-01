package com.nival.chit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Summary of a chit group's financial status as of a given day.
 *
 * <p>All amounts are calculated in real time based on current data:</p>
 * <ul>
 *     <li>Total contributions collected so far</li>
 *     <li>Total loans outstanding (principal + interest up to today)</li>
 *     <li>Approximate group fund balance</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupFinancialSummaryDTO {

    private Long chitGroupId;
    private String chitGroupName;
    private String chitGroupCode;

    private int totalMembers;
    private double monthlyContributionAmount;

    private double totalContributionsCollected;
    private double totalLoansOutstanding;
    private double fundBalanceApprox;

    private LocalDate asOfDate;
}


