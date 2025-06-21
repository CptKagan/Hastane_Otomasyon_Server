package com.hastaneotomasyon.patient_service.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.3.0")
                .withDatabaseName("patientdb")       // uygulamanızdaki DB
                .withUsername("patientuser")         // aynı user/pass
                .withPassword("patientpass")
                .withStartupTimeout(Duration.ofMinutes(3)); // daha uzun bekle
    }
}
