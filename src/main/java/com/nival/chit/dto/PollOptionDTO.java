package com.nival.chit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollOptionDTO {
    private Long id;
    private String optionText;
    private long voteCount;
}
