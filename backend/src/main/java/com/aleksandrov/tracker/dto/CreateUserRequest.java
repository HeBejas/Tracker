package com.aleksandrov.tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserRequest(
        @JsonProperty("full_name") String fullName,
        @JsonProperty("password_hash") String passwordHash,
        @JsonProperty("role_id") Integer roleId,
        String email,
        @JsonProperty("status_id") Integer statusId
) {}