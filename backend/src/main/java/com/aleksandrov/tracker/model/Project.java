package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "projects")
@Data

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonIgnore
    private Workspace workspace;

    @JsonProperty("workspaceId")
    public Long getWorkspaceId() {
        return workspace != null ? workspace.getId() : null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    @JsonIgnore
    private ProjectStatus status;

    @JsonProperty("statusId")
    public Long getStatusId() {
        return status != null ? status.getId() : null;
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "total_tasks_count")
    private Integer totalTasksCount;

    @Column(name = "completed_tasks_count")
    private Integer completedTasksCount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}