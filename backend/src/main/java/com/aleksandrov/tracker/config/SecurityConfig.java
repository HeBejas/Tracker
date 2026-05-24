package com.aleksandrov.tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Говорим Спрингу, что тут лежат настройки безопасности
public class SecurityConfig {

    // ВОТ ЭТОТ МЕТОД СОЗДАЕТ ТОТ САМЫЙ БИН, КОТОРЫЙ ИЩЕТ ТВОЙ КОНТРОЛЛЕР!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Настройка доступов, чтобы Спринг не блокировал запросы к авторизации
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Открываем доступ для логина, инвайтов и системной реги
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}