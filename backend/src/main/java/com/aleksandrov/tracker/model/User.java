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