package com.hastaneotomasyon.doctor_service.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hastaneotomasyon.doctor_service.dto.*;
import com.hastaneotomasyon.doctor_service.service.DoctorService;
import com.hastaneotomasyon.doctor_service.service.KeyCloakClientService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doktor")
@RequiredArgsConstructor
public class DoctorController {
    private final KeyCloakClientService keyCloakClientService;
    private final DoctorService doctorService;

    @GetMapping("/fetchDoktorById/{doktorId}")
    public ResponseEntity<DoctorRequestHastaService> getDoctorById(@PathVariable String doktorId) {
        DoctorRequestHastaService doctor = doctorService.getDoctorById(doktorId);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/randevugoruntule/kod/{randevuKodu}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuGoruntuleKod(@AuthenticationPrincipal Jwt jwt, @PathVariable String randevuKodu){
        return ResponseEntity.ok(doctorService.randevuGoruntuleKod(jwt, randevuKodu));
    }

    @GetMapping("/randevugoruntule/id/{randevuId}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuGoruntuleId(@AuthenticationPrincipal Jwt jwt, @PathVariable Long randevuId){
        return ResponseEntity.ok(doctorService.randevuGoruntuleId(jwt, randevuId));
    }

    @PostConstruct
    public void syncDoctorsOnStartup() {
        doctorService.fetchAllDoctors();
    }

    @GetMapping("/gunlukrandevugoruntule")
    public ResponseEntity<List<RandevuResponseHastaIsimSoyisim>> gunlukRandevuGoruntule(@AuthenticationPrincipal Jwt jwt) {
        List<RandevuResponseHastaIsimSoyisim> randevuResponseHastaIsimSoyisim = doctorService.gunlukRandevuGoruntule(jwt);
        return ResponseEntity.ok(randevuResponseHastaIsimSoyisim);
    }

    @PostMapping("/muayenebaslangici")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> muayeneBaslangici(@AuthenticationPrincipal Jwt jwt, @RequestBody MuayeneBaslangiciRequest muayeneBaslangiciRequest) throws JsonProcessingException {
        RandevuResponseHastaIsimSoyisim randevuResponseHastaIsimSoyisim = doctorService.muayeneBaslangici(jwt, muayeneBaslangiciRequest);
        return ResponseEntity.ok(randevuResponseHastaIsimSoyisim);
    }

    @PatchMapping("/randevupublicnotekleme/{randevuId}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuyaPublicNotEkleOrGuncelle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long randevuId, @RequestBody @Valid NotRequest notRequest){

        return ResponseEntity.ok(doctorService.randevuyaPublicNotEkleOrGuncelle(jwt, randevuId, notRequest));
    }

    @PatchMapping("/randevuprivatenotekleme/{randevuId}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuyaPrivateNotEkleOrGuncelle(@AuthenticationPrincipal Jwt jwt, @PathVariable Long randevuId, @RequestBody @Valid NotRequest notRequest){
        return ResponseEntity.ok(doctorService.randevuyaPrivateNotEkleOrGuncelle(jwt, randevuId, notRequest));
    }

    @PatchMapping("/randevutestekleme/{randevuId}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuTestEkleme(@AuthenticationPrincipal Jwt jwt, @PathVariable Long randevuId, @RequestBody TestRequest testRequest){
        return ResponseEntity.ok(doctorService.randevuTestEkleme(jwt, randevuId, testRequest));
    }

    @PatchMapping("/randevuteshisvereceteekleme/{randevuId}")
    public ResponseEntity<RandevuResponseHastaIsimSoyisim> randevuTeshisVeReveteEkleme(@AuthenticationPrincipal Jwt jwt, @PathVariable Long randevuId, @RequestBody RandevuTeshis randevuTeshis){
        return ResponseEntity.ok(doctorService.randevuTeshisVeReceteEkleme(jwt, randevuId, randevuTeshis));
    }
}
