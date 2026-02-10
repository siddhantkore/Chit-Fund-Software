package com.nival.chit.dto;

import lombok.Data;

@Data
public class VoteDTO {
    private Long pollId;
    private Long optionId;
    private Long userId;
}
