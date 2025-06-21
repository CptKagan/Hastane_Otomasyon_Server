package com.hastaneotomasyon.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank(message = "Refresh token boş olamaz!")
        String refreshToken) {}
