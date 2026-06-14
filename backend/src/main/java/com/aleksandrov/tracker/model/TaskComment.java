package com.aleksandrov.tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "task_comments")
@Data
public class TaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;
    @JsonProperty("taskId")
    public Long getTaskId() {
        return task != null ? task.getId() : null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    @JsonIgnore
    private TaskComment replyTo;
    @JsonProperty("replyId")
    public Long getReplyId() {
        return replyTo != null ? replyTo.getId() : null;
    }

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}