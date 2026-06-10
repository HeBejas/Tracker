package com.aleksandrov.tracker.dto;

import lombok.Data;

@Data
public class CreateProjectCreateDto {
    private String name;
    private Long workspaceId;
    private String status = "new";
}