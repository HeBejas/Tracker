package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.model.ProjectComment;
import com.aleksandrov.tracker.dto.ProjectCommentResponseDto;
import com.aleksandrov.tracker.service.ProjectCommentService;
import com.aleksandrov.tracker.dto.CreateProjectCommentDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/project-comments")
@RequiredArgsConstructor
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;

    @GetMapping
    public ResponseEntity<List<ProjectCommentResponseDto>> getCommentsByProject(@RequestParam Long projectId) {
        List<ProjectCommentResponseDto> comments = projectCommentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<ProjectComment> createComment(
            @RequestBody CreateProjectCommentDto dto,
            @RequestParam Long userId
    ) {
        ProjectComment createdComment = projectCommentService.createComment(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        projectCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}