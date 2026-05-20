//package com.aleksandrov.tracker.dto;
//// То, что отправляет Vue
//package com.aleksandrov.tracker.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//public class AuthRequest {
//    public record LoginRequest(String username, String password) {}
//    public record LoginResponse(String token, String username, String role) {}
//
//    public record CreateUserRequest(
//            @JsonProperty("full_name") String fullName,
//            @JsonProperty("password_hash") String passwordHash,
//            @JsonProperty("role_id") Integer roleId,
//            String email,
//            @JsonProperty("status_id") Integer statusId
//    ) {}
//}