package com.nival.chit.controllers.chitgroup;

import com.nival.chit.services.ChitGroupMemberOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class ChitGroupOperationsController {

    private final ChitGroupMemberOperationsService chitGroupMemberOperationsService;

    public void addMemberToChitGroup() {

    }

    public void removeMemberToChitGroup() {

    }
}
