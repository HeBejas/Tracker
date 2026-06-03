package com.aleksandrov.tracker.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.model.UserRole;
import com.aleksandrov.tracker.model.UserStatus;
import com.aleksandrov.tracker.repository.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public record ManagerInviteRequest(String email, String fullName, Integer workspaceId, Long inviterId) {}
    public record EmployeeInviteRequest(String email, String fullName, Integer workspaceId, Integer projectId, Long inviterId) {}

    public record AuthRequest(String email, String password) {}
    public record InviteRequest(String email, Integer workspaceId, String fullName, Integer roleId, Long inviterId) {}
    public record InviteResponse(String email, String rawPassword, String message) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return ResponseEntity.ok(user.getRole().getId());
    }
    @PostMapping("/invite/manager")
    public ResponseEntity<?> inviteManager(@RequestBody ManagerInviteRequest request) {
        User inviter = userRepository.findById(request.inviterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Только администратор может приглашать менеджеров"));

        if (inviter.getRole().getId() != 1L) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Только администратор может приглашать менеджеров");
        }
        if (userRepository.existsByEmailAndWorkspaceId(request.email(), request.workspaceId())) {
            return ResponseEntity.badRequest().body("Этот менеджер уже существует.");
        }

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        User newManager = createBaseUser(request.email(), request.fullName(), request.workspaceId(), rawPassword);

        UserRole role = new UserRole();
        role.setId(2L);
        newManager.setRole(role);

        userRepository.save(newManager);

        return ResponseEntity.ok(new InviteResponse(newManager.getEmail(), rawPassword, "Менеджер успешно добавлен!"));
    }

    @PostMapping("/invite/employee")
    public ResponseEntity<?> inviteEmployee(@RequestBody EmployeeInviteRequest request) {
        User inviter = userRepository.findById(request.inviterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка"));

        if (inviter.getRole().getId() == 3L) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("У вас нет прав для приглашения сотрудников!");
        }

        if (userRepository.existsByEmailAndWorkspaceId(request.email(), request.workspaceId())) {
            return ResponseEntity.badRequest().body("Сотрудник с таким email уже существует");
        }

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        User newEmployee = createBaseUser(request.email(), request.fullName(), request.workspaceId(), rawPassword);

        UserRole role = new UserRole();
        role.setId(3L);
        newEmployee.setRole(role);

        userRepository.save(newEmployee);

        return ResponseEntity.ok(new InviteResponse(newEmployee.getEmail(), rawPassword, "Сотрудник успешно добавлен на проект!"));
    }

    private User createBaseUser(String email, String fullName, Integer workspaceId, String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setWorkspaceId(workspaceId);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        UserStatus status = new UserStatus();
        status.setId(1L); // Active
        user.setStatus(status);

        return user;
    }
}
//
//@PostMapping("/invite")
//public ResponseEntity<?> inviteUser(@RequestBody InviteRequest request) {
//    User inviter = userRepository.findById(request.inviterId())
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//    if (userRepository.existsByEmailAndWorkspaceId(request.email(), request.workspaceId())) {
//        return ResponseEntity.badRequest().body("Сотрудник с таким email уже участвует в проекте!");
//    }
//
//    String rawPassword = UUID.randomUUID().toString().substring(0, 8);
//    String hashedPassword = passwordEncoder.encode(rawPassword);
//
//    User newUser = new User();
//    newUser.setPasswordHash(hashedPassword);
//    newUser.setEmail(request.email());
//    newUser.setFullName(request.fullName());
//    newUser.setWorkspaceId(request.workspaceId());
//
//    UserRole role = new UserRole();
//    role.setId(3L);
//    newUser.setRole(role);
//
//    UserStatus status = new UserStatus();
//    status.setId(1L);
//    newUser.setStatus(status);
//
//    userRepository.save(newUser);
//
//    return ResponseEntity.ok(new InviteResponse(
//            newUser.getEmail(),
//            rawPassword,
//            "Сотрудник успешно добавлен!"
//    ));
//}