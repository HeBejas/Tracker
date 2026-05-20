//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            // Вызываем логику проверки
//            LoginResponse response = authService.authenticate(request);
//            return ResponseEntity.ok(response);
//
//        } catch (DisabledAccountException e) {
//            // Учетная запись отключена
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Учетная запись заблокирована. Обратитесь к администратору.");
//
//        } catch (BadCredentialsException e) {
//            // Неверный логин или пароль
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Неверное имя пользователя или пароль.");
//        }
//    }
//}
package com.aleksandrov.tracker.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;
import com.aleksandrov.tracker.repository.UserRepository;
import com.aleksandrov.tracker.repository.UserRoleRepository;
import com.aleksandrov.tracker.entity.User;
import com.aleksandrov.tracker.entity.UserRole;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmailAndPasswordHash(
                request.email(),
                request.passwordHash()
        );
//        System.out.println("DEBUG email: '" + request.email() + "'");
//        System.out.println("DEBUG passwordHash: '" + request.passwordHash() + "'");

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            UserRole userRole = userRoleRepository.findById(user.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            String roleName = userRole.getName();

            return ResponseEntity.ok(Map.of("success", true, "role", roleName));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Неверный логин или пароль"
            ));
        }
    }

    // Вспомогательный класс для приема данных
    public record LoginRequest(String email, String passwordHash) {}
}