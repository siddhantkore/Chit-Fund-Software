package com.nival.chit.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Loan‑related views for members and group administrators.
 *
 * <p>Planned responsibilities:</p>
 * <ul>
 *     <li>List all members within a chit group who have taken loans</li>
 *     <li>Expose current payable amounts (principal + interest) per user and per group</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/chit/loan/")
@Tag(name = "Loans", description = "View and track member loans across chit groups.")
public class MemberLoanController {

    /**
     * Will give all the members of specified 'chit group' who have taken loan
     * generally for group admin
     */
    public void seeAllMembersWhoHaveTakenLoan() {

    }

    /**
     * will give principle + total interest in specified 'chit group'
     * generally for individual user
     */
    public void getCurrentPayable(){

    }

}
