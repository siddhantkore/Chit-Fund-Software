package com.nival.chit.controllers;

import com.nival.chit.dto.GroupFinancialSummaryDTO;
import com.nival.chit.dto.MemberGroupFinancialSummaryDTO;
import com.nival.chit.services.ReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Reporting endpoints for real-time (daily) financial summaries.
 *
 * <p>These endpoints provide an easy-to-understand overview of what has happened
 * in each group and for each member, recalculated on every request as of \"today\".</p>
 */
@RestController
@RequestMapping("/api/chit/report/")
@RequiredArgsConstructor
@Tag(name = "Reporting", description = "Real-time (daily) financial summaries for groups and members.")
public class ReportingController {

    private final ReportingService reportingService;

    /**
     * Get real-time financial summary for a chit group.
     *
     * @param groupId chit group id
     * @return financial summary for the group
     */
    @GetMapping("/group/{groupId}")
    @Operation(
            summary = "Get chit group financial summary",
            description = "Returns a real-time financial summary for the specified chit group as of today, " +
                          "including total contributions collected, loans outstanding, and approximate fund balance."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved group summary",
                    content = @Content(schema = @Schema(implementation = GroupFinancialSummaryDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Chit group not found")
    })
    public ResponseEntity<GroupFinancialSummaryDTO> getGroupSummary(@PathVariable Long groupId) {
        GroupFinancialSummaryDTO summary = reportingService.getGroupSummary(groupId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Get real-time financial summaries for a member across all their chit groups.
     *
     * @param userId user id
     * @return list of per-group financial summaries
     */
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get member financial summaries across groups",
            description = "Returns a list of financial summaries for the specified member across all chit groups " +
                          "where they are a member, including total contributions, current loan payable, and " +
                          "estimated profit share from group interest."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved member summaries",
                    content = @Content(schema = @Schema(implementation = MemberGroupFinancialSummaryDTO.class))
            )
    })
    public ResponseEntity<List<MemberGroupFinancialSummaryDTO>> getMemberGroupSummaries(@PathVariable Long userId) {
        List<MemberGroupFinancialSummaryDTO> summaries = reportingService.getMemberGroupSummaries(userId);
        return ResponseEntity.ok(summaries);
    }
}


