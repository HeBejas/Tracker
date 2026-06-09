package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.model.UserRole;
import com.aleksandrov.tracker.model.UserStatus;
import com.aleksandrov.tracker.repository.UserRepository;
import com.aleksandrov.tracker.repository.UserRoleRepository;
import com.aleksandrov.tracker.repository.UserStatusRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    public record InviteResponse(String email, String rawPassword, String message) {}
    public record LoginResponse(Long userId, String role, String fullName, String email, Integer workspaceId) {}

    public InviteResponse inviteEmployee(String email, String fullName, Integer workspaceId) {
        if (userRepository.existsByEmail(email)) { throw new RuntimeException("Email уже занят"); }
        UserRole role = userRoleRepository.findById(3L).orElseThrow();
        UserStatus status = userStatusRepository.findById(1L).orElseThrow();
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setWorkspaceId(workspaceId);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setStatus(status);

        userRepository.save(user);
        return new InviteResponse(email, rawPassword, "Сотрудник успехно добавлен");
    }

    public InviteResponse inviteManager(String email, String fullName, Integer workspaceId) {
        if (userRepository.existsByEmail(email)) { throw new RuntimeException("Email уже занят"); }
        UserRole role = userRoleRepository.findById(2L).orElseThrow();
        UserStatus status = userStatusRepository.findById(1L).orElseThrow();
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setWorkspaceId(workspaceId);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setStatus(status);

        userRepository.save(user);
        return new InviteResponse(email, rawPassword, "Менеджер успехно добавлен");
    }

    public User inviteAdmin(String email, String fullName, String rawPassword){
        if (userRepository.existsByEmail(email)) { throw new RuntimeException("Email уже занят"); }
        UserRole role = userRoleRepository.findById(1L).orElseThrow();
        UserStatus status = userStatusRepository.findById(1L).orElseThrow();

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setStatus(status);

        return userRepository.save(user);
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPasswordHash()))
                .orElseThrow(() -> new RuntimeException("Неверный email или пароль"));
        return new LoginResponse(
                user.getId(),
                user.getRole().getName(),
                user.getFullName(),
                user.getEmail(),
                user.getWorkspaceId()
        );
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Неверный пароль");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
