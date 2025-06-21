package com.hastaneotomasyon.lab_service.service;

import com.hastaneotomasyon.lab_service.config.KeycloakConfigProperties;
import com.hastaneotomasyon.lab_service.dto.LabResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KeycloakClientService {
    private final RestClient restClient;
    private final KeycloakConfigProperties keycloakConfigProperties;
    private static final Set<String> SYSTEM_ROLES = Set.of("offline_access", "uma_authorization", "default-roles-hastane-otomasyon", "doktor");
    private static final Set<String> COMPOSITE_LAB_ROLES = Set.of(
            "biyokimya-lab",
            "goruntuleme-lab",
            "goz-lab",
            "kan-lab",
            "hormon-lab"
    );


    public String getAdminToken() {
        String tokenUrl = UriComponentsBuilder.fromUriString(keycloakConfigProperties.getAuthServerUrl())
                .pathSegment("realms", keycloakConfigProperties.getRealm(), "protocol", "openid-connect", "token")
                .toUriString();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", keycloakConfigProperties.getClientId());
        body.add("client_secret", keycloakConfigProperties.getClientSecret());

        try {
            Map response = restClient.post()
                    .uri(tokenUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }
            throw new RuntimeException("Failed to get access token from Keycloak");
        } catch (Exception e) {
            throw new RuntimeException("Error getting admin token: " + e.getMessage(), e);
        }
    }

    public List<LabResponse> fetchAllLabUsers() {
        String token = getAdminToken();

        String allUsersUrl = UriComponentsBuilder.fromUriString(keycloakConfigProperties.getAuthServerUrl())
                .pathSegment("admin", "realms", keycloakConfigProperties.getRealm(), "users")
                .toUriString();

        List<Map<String, Object>> allUsers = restClient.get()
                .uri(allUsersUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        System.out.println("🔍 Tüm kullanıcı sayısı: " + allUsers.size());

        List<LabResponse> responses = new ArrayList<>();

        for (Map<String, Object> user : allUsers) {
            String id = (String) user.get("id");

            String rolesUrl = UriComponentsBuilder.fromUriString(keycloakConfigProperties.getAuthServerUrl())
                    .pathSegment("admin", "realms", keycloakConfigProperties.getRealm(), "users", id, "role-mappings", "realm")
                    .toUriString();

            List<Map<String, Object>> roles = restClient.get()
                    .uri(rolesUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            // Bu kullanıcı lab mı?
            boolean isLabUser = roles.stream()
                    .map(r -> (String) r.get("name"))
                    .anyMatch(role -> role.equals("lab") || COMPOSITE_LAB_ROLES.contains(role));

            if (!isLabUser) continue;

            String department = roles.stream()
                    .map(r -> (String) r.get("name"))
                    .filter(role -> !SYSTEM_ROLES.contains(role) && !role.equals("lab"))
                    .findFirst()
                    .orElse("bilinmiyor");

            responses.add(new LabResponse(
                    id,
                    (String) user.get("username"),
                    (String) user.get("firstName"),
                    (String) user.get("lastName"),
                    (String) user.get("email"),
                    department
            ));
        }

        return responses;
    }
}
