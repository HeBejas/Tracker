//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping
//    // Доступ только для Админов и Менеджеров
//    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
//    public ResponseEntity<String> createUser(
//            @RequestBody CreateUserRequest request,
//            Principal principal // Достаем текущего авторизованного пользователя
//    ) {
//        // В реальном проекте роль лучше доставать из Authentication/JWT Token
//        String currentUserRole = getCurrentUserRole(principal);
//
//        userService.createUser(request, currentUserRole);
//        return ResponseEntity.ok("Пользователь успешно создан");
//    }
//}

package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.dto.CreateUserRequest;
import com.aleksandrov.tracker.entity.User;
import com.aleksandrov.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    // ДОБАВИТЬ ПОЛЬЗОВАТЕЛЯ
    @PostMapping
    public String createAnyUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setPasswordHash(request.passwordHash());
        user.setRoleId(request.roleId());
        user.setEmail(request.email());
        user.setStatusId(request.statusId());

        userRepository.save(user);

        return "Пользователь " + request.fullName() + " успешно создан!";
    }
    // УДАЛИТЬ ПОЛЬЗОВАТЕЛЯ
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return "Пользователь с ID " + id + " не найден.";
        }
        userRepository.deleteById(id);
        return "Пользователь с ID " + id + " удален.";
    }
}