package com.nival.chit.dto;

import com.nival.chit.enums.MediaType;
import lombok.Data;

@Data
public class UploadMediaDTO {
    private Long groupId;
    private Long uploaderId;
    private String url;
    private String caption;
    private MediaType mediaType;
}
