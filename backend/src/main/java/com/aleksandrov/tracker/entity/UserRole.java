package com.aleksandrov.tracker.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "user_roles")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}