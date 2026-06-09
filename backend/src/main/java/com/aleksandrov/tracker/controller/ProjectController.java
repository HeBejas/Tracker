package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.model.Project;
import com.aleksandrov.tracker.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor

public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getProjectsByWorkspace(@RequestParam Long workspaceId) {
        List<Project> projects = projectService.getProjectsByWorkspaceId(workspaceId);
        return ResponseEntity.ok(projects);
    }

}