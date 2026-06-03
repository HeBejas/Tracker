package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "workspace_statuses")
@Data

public class WorkspaceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
