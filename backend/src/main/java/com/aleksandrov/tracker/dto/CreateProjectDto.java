package com.aleksandrov.tracker.dto;

import lombok.Data;

@Data
public class CreateProjectDto {
    private String name;
    private Long workspaceId;
    private String status = "new";
}