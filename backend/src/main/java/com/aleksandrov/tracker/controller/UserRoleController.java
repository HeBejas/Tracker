package com.aleksandrov.tracker.controller;

import com.aleksandrov.tracker.entity.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRoleController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/roles")
    public List<UserRole> getRoles() {
        return entityManager.createQuery("SELECT r FROM UserRole r", UserRole.class).getResultList();
    }
}