package com.hastaneotomasyon.auth_service.dto;

public record TokenResponse(
        String access_token,
        String refresh_token,
        Integer expires_in) {}
