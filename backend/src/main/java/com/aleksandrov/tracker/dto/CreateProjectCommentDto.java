package com.aleksandrov.tracker.dto;

import lombok.Data;

@Data
public class CreateProjectCommentDto {
    private Long projectId;
    private String message;
    private Long replyId;
}