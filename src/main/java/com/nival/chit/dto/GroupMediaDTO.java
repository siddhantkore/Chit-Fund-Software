package com.nival.chit.dto;

import com.nival.chit.enums.MediaType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupMediaDTO {
    private Long id;
    private Long groupId;
    private Long uploaderId;
    private String uploaderName;
    private String url;
    private String caption;
    private MediaType mediaType;
    private LocalDateTime createdAt;
}
