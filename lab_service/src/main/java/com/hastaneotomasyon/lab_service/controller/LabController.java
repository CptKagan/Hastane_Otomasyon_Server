package com.hastaneotomasyon.lab_service.controller;

import com.hastaneotomasyon.lab_service.dto.TestResponse;
import com.hastaneotomasyon.lab_service.dto.TestSonucuRequest;
import com.hastaneotomasyon.lab_service.dto.TestSonucuResponse;
import com.hastaneotomasyon.lab_service.service.KeycloakClientService;
import com.hastaneotomasyon.lab_service.service.LabService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratuvar")
@RequiredArgsConstructor
public class LabController {
    private final KeycloakClientService keycloakClientService;
    private final LabService labService;

    @PostConstruct
    public void syncLabUsersOnStartup(){
        labService.fetchAllLabUsers();
    }

    @GetMapping("/gunluktestgoruntule")
    public ResponseEntity<List<TestResponse>> gunlukTestGoruntule(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(labService.gunlukTestgoruntule(jwt));
    }

    @PostMapping("/testsonucuekle")
    public ResponseEntity<TestSonucuResponse> testSonucuEkle(@AuthenticationPrincipal Jwt jwt, @RequestBody TestSonucuRequest testSonucuRequest){
        TestSonucuResponse testSonucuResponse = labService.testSonucuEkle(jwt, testSonucuRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(testSonucuResponse);
    }

    @GetMapping("/doktortestsonucugoruntule/{randevuId}")
    public ResponseEntity<List<TestSonucuResponse>> doktorTestSonucuGoruntule(@RequestHeader("Authorization") String bearerToken, @PathVariable Long randevuId){
        return ResponseEntity.ok(labService.doktorTestSonucuGoruntule(randevuId));
    }
}
