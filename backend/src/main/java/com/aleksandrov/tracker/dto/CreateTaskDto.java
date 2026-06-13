package com.aleksandrov.tracker.dto;

import lombok.Data;

@Data
public class CreateTaskDto {
    private String name;
    private Long projectId;
    private Long parentTaskId;
    private String status = "low";
}