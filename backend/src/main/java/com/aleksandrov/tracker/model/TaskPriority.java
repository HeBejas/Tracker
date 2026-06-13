package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_priorities")
@Data

public class TaskPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}