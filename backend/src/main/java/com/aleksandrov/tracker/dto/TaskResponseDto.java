package com.aleksandrov.tracker.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class TaskResponseDto {
    private Long id;
    private String name;
    private Long authorId;
    private String authorName;
    private Long projectId;
    private String projectName;
    private Long statusId;
    private Long priorityId;
    private OffsetDateTime deadlineDate;
    private OffsetDateTime createdAt;
}