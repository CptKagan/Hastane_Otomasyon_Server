package com.hastaneotomasyon.doctor_service.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakConfigProperties {
    private String authServerUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
}