package com.nival.chit.dto;

import com.nival.chit.enums.PollStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PollDTO {
    private Long id;
    private Long groupId;
    private String groupName;
    private Long creatorId;
    private String creatorName;
    private String question;
    private PollStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private List<PollOptionDTO> options;
}
