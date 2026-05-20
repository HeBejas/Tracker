//package com.aleksandrov.tracker.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpStatus;
//import lombok.RequiredArgsConstructor;
//import lombok.Data;
//import com.aleksandrov.tracker.repository.UserRepository;
//import com.aleksandrov.tracker.model.UserSession;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * ЧТО ЭТО: REST-контроллер авторизации.
// * ЗАЧЕМ НУЖНО: Принимает HTTP POST-запросы с сайта на URL "/api/auth/login", проверяет пароль и возвращает JSON-ответ фронтенду.
// */
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*") // Разрешаем фронтенду слать запросы на бэкенд
//public class LoginController {
//
//    private final UserRepository userRepository;
//
//    // Вспомогательный класс-DTO для приема данных (Spring сам заполнит его из JSON)
//    @Data
//    public static class LoginRequest {
//        private String email;
//        private String password;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
//        String passwordHash = hashPassword(request.getPassword());
//        Optional<String> roleNameOpt = userRepository.findUserRole(request.getEmail(), passwordHash);
//
//        if (roleNameOpt.isPresent()) {
//            UserSession.startSession(request.getEmail(), roleNameOpt.get());
//            // Возвращаем сайту статус 200 OK и данные роли в формате JSON
//            return ResponseEntity.ok(Map.of(
//                    "success", true,
//                    "role", roleNameOpt.get()
//            ));
//        }
//
//        // Если не нашли — возвращаем ошибку 401 (Не авторизован)
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
//                "success", false,
//                "message", "Неверный логин или пароль"
//        ));
//    }
//
//    private String hashPassword(String password) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest(password.getBytes());
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hash) {
//                String hex = Integer.toHexString(0xff & b);
//                if (hex.length() == 1) hexString.append('0');
//                hexString.append(hex);
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Ошибка шифрования", e);
//        }
//    }
//}