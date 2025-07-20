package com.hastaneotomasyon.auth_service.controller;

import com.hastaneotomasyon.auth_service.dto.LoginRequest;
import com.hastaneotomasyon.auth_service.dto.LogoutRequest;
import com.hastaneotomasyon.auth_service.dto.RefreshTokenRequest;
import com.hastaneotomasyon.auth_service.dto.TokenResponse;
import com.hastaneotomasyon.auth_service.service.KeycloakAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakAuthService keycloakAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        TokenResponse token = keycloakAuthService.getToken(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest logoutRequest){
        keycloakAuthService.logout(logoutRequest.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        TokenResponse tokenResponse = keycloakAuthService.refreshAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
