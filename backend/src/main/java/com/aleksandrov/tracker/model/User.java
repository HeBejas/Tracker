package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity // Этот класс отображение таблицы
@Table(name = "users") // Таблица в бд "users"
@Data //Lombok создает шаблонные команды
public class User {

    @Id // Отмечает поле PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //указывает стратегию автоматического создания ID
    private Long id;

    @Column(name = "workspace_id") // АННОТАЦИЯ. Параметры колонки
    private Integer workspaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private UserStatus status;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}