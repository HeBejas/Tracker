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

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody InviteRequest request) {
        User inviter = userRepository.findById(request.inviterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userRepository.existsByEmailAndWorkspaceId(request.email(), request.workspaceId())) {
            return ResponseEntity.badRequest().body("Сотрудник с таким email уже участвует в проекте!");
        }

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User();
        newUser.setPasswordHash(hashedPassword);
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());
        newUser.setWorkspaceId(request.workspaceId());

        UserRole role = new UserRole();
        role.setId(3L);
        newUser.setRole(role);

        UserStatus status = new UserStatus();
        status.setId(1L);
        newUser.setStatus(status);


        // =================================

        userRepository.save(newUser);

        return ResponseEntity.ok(new InviteResponse(
                newUser.getEmail(),
                rawPassword,
                "Сотрудник успешно добавлен!"
        ));
    }
}