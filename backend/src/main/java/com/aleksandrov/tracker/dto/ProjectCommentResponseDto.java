package com.aleksandrov.tracker.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class ProjectCommentResponseDto {
    private Long id;
    private Long projectId;
    private Long userId;
    private String authorName;
    private Long replyId;
    private String message;
    private OffsetDateTime createdAt;
}