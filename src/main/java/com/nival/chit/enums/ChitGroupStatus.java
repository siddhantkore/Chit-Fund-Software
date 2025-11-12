package com.nival.chit.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChitGroupStatus {
    ACTIVE("Group is active"),
    COMPLETED("Group Completed and closed"),
    PENDING("Group Operation is pending"),
    INACTIVE("Group is inactive"); // try to give duration

    private final String description;
}
