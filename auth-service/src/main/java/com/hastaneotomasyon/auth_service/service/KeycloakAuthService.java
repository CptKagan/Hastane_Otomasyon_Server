package com.hastaneotomasyon.auth_service.service;

import com.hastaneotomasyon.auth_service.config.KeycloakConfigProperties;
import com.hastaneotomasyon.auth_service.dto.LoginRequest;
import com.hastaneotomasyon.auth_service.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService {
    private final RestClient restClient;
    private final KeycloakConfigProperties keycloakConfigProperties;

    public TokenResponse getToken(LoginRequest loginRequest){
        String tokenUrl = UriComponentsBuilder.fromUriString(keycloakConfigProperties.getAuthServerUrl())
                .pathSegment("realms", keycloakConfigProperties.getRealm(), "protocol", "openid-connect", "token")
                .toUriString();

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Body
        // Aynı key birden çok value tutabilir. Normal map'ten farklı.
        // MultiValueMap bekliyor.
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", keycloakConfigProperties.getClientId());
        body.add("client_secret", keycloakConfigProperties.getClientSecret());
        body.add("username", loginRequest.username());
        body.add("password", loginRequest.password());

        try{
            Map response = restClient.post()
                    .uri(tokenUrl)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            String accessToken = (String) response.get("access_token");
            String refreshToken = (String) response.get("refresh_token");
            Integer expiresIn = (Integer) response.get("expires_in");
            return new TokenResponse(accessToken, refreshToken, expiresIn);
        } catch (Exception e){
            System.out.println("Token URL: " + tokenUrl);
            System.out.println("İstek body: " + body);
            e.printStackTrace();
            throw new RuntimeException("Keycloak token alınamadı: " + e.getMessage());
        }
    }

    public void logout(String refreshToken){
        String logoutUrl = UriComponentsBuilder.fromUriString(keycloakConfigProperties.getAuthServerUrl())
                .pathSegment("realms", keycloakConfigProperties.getRealm(), "protocol", "openid-connect", "logout")
                .toUriString();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", keycloakConfigProperties.getClientId());
        form.add("client_secret", keycloakConfigProperties.getClientSecret());
        form.add("refresh_token", refreshToken);

        restClient.post()
                .uri(logoutUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(form)
                .retrieve()
                .body(Void.class);
    }
}
