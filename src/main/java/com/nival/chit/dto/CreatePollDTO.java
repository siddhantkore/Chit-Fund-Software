package com.nival.chit.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePollDTO {
    private Long groupId;
    private Long creatorId;
    private String question;
    private List<String> options;
    private LocalDateTime expiresAt;
}
