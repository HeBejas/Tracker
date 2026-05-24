package com.aleksandrov.tracker.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.repository.UserRepository;
import java.util.UUID; // Для генерации случайного пароля

//Добавление пользователя
//Авторизация
//Создание сессии . . . (?)

@RestController // @Controller + @ResponseBody для REST API
@RequestMapping("/api/auth") // базовый путь для всех методов
@RequiredArgsConstructor // Связь с репозиторием


public class AuthController {

    private final UserRepository  userRepository ;
    private final PasswordEncoder passwordEncoder;

    public record AuthRequest(String email, String password) {}
    public record InviteRequest(String email, Integer workspaceId, String fullName, Integer roleId, Long inviterId) {}
    // record создает immutable контейнер, автоматически генерирует базовые методы, гетеры и сетеры
    public record InviteResponse(String email, String rawPassword, String message) {}

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthRequest request) {
        //@RequestBody преобразует входящий http запрос в java
        User user = userRepository.findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return ResponseEntity.ok(user.getRoleId());
    }
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody InviteRequest request) {
        User inviter = userRepository.findById(request.inviterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        if (inviter.getRoleId() > 2) {
//            return ResponseEntity.status(403).body("У вас нет прав для приглашения сотрудников");
//        }
        //ДОБАВИТЬ ТЭГИ

        if (userRepository.existsByEmailAndWorkspaceId(request.email(), request.workspaceId())) {
            return ResponseEntity.badRequest().body("Сотрудник с таким email уже учавствует в проекте!");
        }
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User();
        newUser.setPasswordHash(hashedPassword);
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());
        newUser.setRoleId(3);
        newUser.setStatusId(1);

        newUser.setWorkspaceId(request.workspaceId());
        userRepository.save(newUser);

        return ResponseEntity.ok(new InviteResponse(
                //ОТПРАВКА ПИСЬМА НА ЕМАИЛ...
                newUser.getEmail(),
                rawPassword,
                "Сотрудник успешно добавлен!"
        ));
    }
//    @PostMapping("/system/reg")
}
