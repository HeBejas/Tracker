//Синглтон, который будет держать в памяти инфу о том, кто сейчас залогинен.
package com.aleksandrov.tracker.model;

import lombok.Getter;

public class UserSession {
    private static UserSession instance;

    @Getter private final String email;
    @Getter private final String role;

    private UserSession(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public static void startSession(String email, String role) {
        instance = new UserSession(email, role);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }
}