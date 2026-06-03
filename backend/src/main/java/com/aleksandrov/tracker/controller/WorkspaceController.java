package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.model.Workspace;
import com.aleksandrov.tracker.model.WorkspaceStatus;
import com.aleksandrov.tracker.model.Tariff;
import com.aleksandrov.tracker.repository.WorkspaceRepository;
import com.aleksandrov.tracker.repository.WorkspaceStatusRepository;
import com.aleksandrov.tracker.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceStatusRepository workspaceStatusRepository;
    private final TariffRepository tariffRepository;

    @GetMapping
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return ResponseEntity.ok(workspaces);
    }

    @PostMapping
    public ResponseEntity<?> createWorkspace(@RequestBody Map<String, Object> body) {
        try {
            Workspace workspace = new Workspace();

            Object tariffObj = body.get("tariff");
            Long tariffId = tariffObj instanceof Map
                    ? ((Number) ((Map) tariffObj).get("id")).longValue()
                    : ((Number) tariffObj).longValue();

            Tariff tariff = tariffRepository.findById(tariffId)
                    .orElseThrow(() -> new RuntimeException("Тариф не найден"));
            workspace.setTariff(tariff);

            Object statusObj = body.get("status");
            Long statusId = statusObj instanceof Map
                    ? ((Number) ((Map) statusObj).get("id")).longValue()
                    : ((Number) statusObj).longValue();

            WorkspaceStatus status = workspaceStatusRepository.findById(statusId)
                    .orElseThrow(() -> new RuntimeException("Статус не найден"));
            workspace.setStatus(status);

            Workspace saved = workspaceRepository.save(workspace);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long id) {
        try {
            workspaceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка: " + e.getMessage());
        }
    }

}