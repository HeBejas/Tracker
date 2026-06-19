package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.service.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    public record EmployeeInviteRequest(String email, String fullName, Integer workspaceId) {}
    public record ManagerInviteRequest(String email, String fullName, Integer workspaceId) {}
    public record AdminInviteRequest(String email, String fullName, String password) {}
    public record ChangePasswordRequest(String oldPassword, String newPassword) {}
    public record AuthRequest(String email, String password) {}
    public record InviteRequest(String email, Integer workspaceId, String fullName, Integer roleId, Long inviterId) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request.email(), request.password()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-password/{userId}")
        try {
            authService.changePassword(userId, request.oldPassword(), request.newPassword());
            return ResponseEntity.ok("Пароль успешно изменён");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite/employee")
    public ResponseEntity<?> inviteEmployee(@RequestBody EmployeeInviteRequest request) {
        try{
            AuthService.InviteResponse response = authService.inviteEmployee(request.email(), request.fullName(), request.workspaceId());
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite/manager")
    public ResponseEntity<?> inviteManager(@RequestBody ManagerInviteRequest request) {
        try{
            AuthService.InviteResponse response = authService.inviteManager(request.email(), request.fullName(), request.workspaceId());
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite/admin")
    public ResponseEntity<?> inviteAdmin(@RequestBody AdminInviteRequest request) {
        try{
            authService.inviteAdmin(request.email(), request.fullName(), request.password());
            return ResponseEntity.ok("Администратор успехно добавлен");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
