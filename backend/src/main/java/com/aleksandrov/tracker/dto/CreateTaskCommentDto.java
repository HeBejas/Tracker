package com.aleksandrov.tracker.dto;

import lombok.Data;

@Data
public class CreateTaskCommentDto {
    private Long taskId;
    private String message;
    private Long replyId;
}