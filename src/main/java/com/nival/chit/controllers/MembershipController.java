package com.nival.chit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chit/membership/")
public class MembershipController {

    /**
     * will give all the groups of which the current user is member
     */
    @GetMapping("/")
    public void getAllChitGroups() {

    }
}
