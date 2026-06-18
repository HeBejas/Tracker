package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.dto.CreateTaskDto;
import com.aleksandrov.tracker.dto.TaskParticipantDto;
import com.aleksandrov.tracker.dto.TaskParticipantResponseDto;
import com.aleksandrov.tracker.dto.TaskResponseDto;

import com.aleksandrov.tracker.model.Task;
import com.aleksandrov.tracker.service.TaskService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor

public class TaskController {

    private final TaskService taskService;

    @GetMapping("/workspace")
    public ResponseEntity<List<Map<String, Object>>> getTasksByWorkspace(@RequestParam Long workspaceId) {
        List<Map<String, Object>> tasks = taskService.getTasksByWorkspace(workspaceId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasksByProject(@RequestParam Long projectId) {
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody CreateTaskDto dto,
            @RequestParam Long authorId
    ) {
        Task createdTask = taskService.createTask(dto, authorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Task updatedTask = taskService.updateTaskFields(id, updates);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<TaskParticipantResponseDto> addTaskParticipant(
            @PathVariable Long id,
            @RequestBody TaskParticipantDto participant
    ) {
        TaskParticipantResponseDto response = taskService.addTaskParticipant(id, participant);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}