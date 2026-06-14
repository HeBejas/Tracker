package com.aleksandrov.tracker.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class TaskCommentResponseDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String authorName;
    private Long replyId;
    private String message;
    private OffsetDateTime createdAt;
}