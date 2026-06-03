package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_statuses")
@Data
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}