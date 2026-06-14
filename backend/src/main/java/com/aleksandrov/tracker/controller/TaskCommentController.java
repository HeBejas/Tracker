package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.model.TaskComment;
import com.aleksandrov.tracker.dto.TaskCommentResponseDto;
import com.aleksandrov.tracker.service.TaskCommentService;
import com.aleksandrov.tracker.dto.CreateTaskCommentDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/task-comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @GetMapping
    public ResponseEntity<List<TaskCommentResponseDto>> getCommentsByTask(@RequestParam Long taskId) {
        List<TaskCommentResponseDto> comments = taskCommentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<TaskComment> createComment(
            @RequestBody CreateTaskCommentDto dto,
            @RequestParam Long userId
    ) {
        TaskComment createdComment = taskCommentService.createComment(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        taskCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}