package com.nival.chit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chit/loan/")
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
