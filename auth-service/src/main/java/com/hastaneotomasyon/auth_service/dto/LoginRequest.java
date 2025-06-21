package com.hastaneotomasyon.auth_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(min = 5, message = "Kullanıcı adı en az 5 karakter olabilir.")
        String username,

        @NotBlank(message = "Şifre boş olamaz.")
        String password) {}
