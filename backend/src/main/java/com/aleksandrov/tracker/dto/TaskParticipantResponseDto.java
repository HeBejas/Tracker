package com.aleksandrov.tracker.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class TaskParticipantResponseDto {
    private Long userId;
    private String userName;
    private Long roleId;
    private OffsetDateTime assignedAt;
}
