package com.aleksandrov.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workspace_id")
    private Integer workspaceId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}