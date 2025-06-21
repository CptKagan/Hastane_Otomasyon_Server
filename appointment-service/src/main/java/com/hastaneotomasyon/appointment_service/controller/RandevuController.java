package com.hastaneotomasyon.appointment_service.controller;

import com.hastaneotomasyon.appointment_service.dto.*;
import com.hastaneotomasyon.appointment_service.service.RandevuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/randevu")
@RequiredArgsConstructor
public class RandevuController {
    private final RandevuService randevuService;

    @PostMapping("/tarihkontrol")
    public ResponseEntity<Boolean> tarihKontrol(@RequestBody @Valid RandevuTarihKontrolRequest randevuTarihKontrolRequest){
        return ResponseEntity.ok(randevuService.tarihKontrol(randevuTarihKontrolRequest));
    }

    @PostMapping("/randevual")
    public ResponseEntity<RandevuResponse> randevuAl(@RequestBody @Valid RandevuRequest randevuRequest){
        RandevuResponse randevuResponse = randevuService.randevuAl(randevuRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(randevuResponse);
    }

    @GetMapping("/randevugoruntule/kod/{randevuKodu}")
    public ResponseEntity<RandevuResponse> randevuGoruntuleKod(@PathVariable String randevuKodu){
        RandevuResponse randevuResponse = randevuService.randevuGoruntuleKod(randevuKodu);
        return ResponseEntity.ok(randevuResponse);
    }

    @GetMapping("randevugoruntule/id/{randevuId}")
    public ResponseEntity<RandevuResponse> randevuGoruntuleId(@PathVariable Long randevuId){
        RandevuResponse randevuResponse = randevuService.randevuGoruntuleId(randevuId);
        return ResponseEntity.ok(randevuResponse);
    }

    @GetMapping("/randevugoruntule/doktor/kod/{randevuKodu}")
    public ResponseEntity<DoktorRandevuResponse> randevuGoruntuleDoktorKod(@PathVariable String randevuKodu, @RequestParam String doktorId){
        return ResponseEntity.ok(randevuService.randevuGoruntuleDoktorKod(randevuKodu, doktorId));
    }

    @GetMapping("/randevugoruntule/doktor/id/{randevuId}")
    public ResponseEntity<DoktorRandevuResponse> randevuGoruntuleDoktorId(@PathVariable Long randevuId, @RequestParam String doktorId){
        return ResponseEntity.ok(randevuService.randevuGoruntuleDoktorId(randevuId, doktorId));
    }

    @GetMapping("/gunlukrandevugoruntule/{doktorId}")
    public ResponseEntity<List<DoktorRandevuResponse>> gunlukRandevuGoruntule(@PathVariable String doktorId){
        return ResponseEntity.ok(randevuService.gunlukRandevuGoruntule(doktorId));
    }

    @PutMapping("/randevuIptal/{randevuId}")
    public ResponseEntity<?> randevuIptal(@PathVariable Long randevuId){
        randevuService.randevuIptal(randevuId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/randevubaslangicibildir/{randevuId}")
    public ResponseEntity<DoktorRandevuResponse> randevuBaslangiciBildir(@PathVariable Long randevuId, @RequestParam String doktorId){
        DoktorRandevuResponse doktorRandevuResponse = randevuService.randevuBaslangiciBildir(randevuId, doktorId);
        return ResponseEntity.ok(doktorRandevuResponse);
    }

    @PatchMapping("/randevupublicnotekleme/{randevuId}")
    public ResponseEntity<DoktorRandevuResponse> randevuPublicNotEkleme(@PathVariable Long randevuId, @RequestBody NotRequest notRequest, @RequestParam String doktorId){
        return ResponseEntity.ok(randevuService.randevuPublicNotEkleme(randevuId, notRequest, doktorId));
    }

    @PatchMapping("/randevuprivatenotekleme/{randevuId}")
    public ResponseEntity<DoktorRandevuResponse> randevuPrivateNotEkleme(@PathVariable Long randevuId, @RequestBody NotRequest notRequest, @RequestParam String doktorId){
        return ResponseEntity.ok(randevuService.randevuPrivateNotEkleme(randevuId, notRequest, doktorId));
    }

    @PatchMapping("/randevutestekleme/{randevuId}")
    public ResponseEntity<DoktorRandevuResponse> randevuTestEkleme(@PathVariable Long randevuId, @RequestBody TestRequest testRequest, @RequestParam String doktorId){
        return ResponseEntity.ok(randevuService.randevuTestEkleme(randevuId, testRequest, doktorId));
    }

    @PostMapping("/gunluktestgoruntule")
    public ResponseEntity<List<TestResponse>> gunlukTestGoruntule(@RequestBody GunlukTestRequest gunlukTestRequest){
        return ResponseEntity.ok(randevuService.gunlukTestGoruntule(gunlukTestRequest));
    }

    @PostMapping("/tumtestlersonuclanmismi")
    public void tumTestlerSonuclanmisMi(@RequestBody TumTestlerSonuclanmisMiRequest tumTestlerSonuclanmisMiRequest){
        randevuService.tumTestlerSonuclanmisMi(tumTestlerSonuclanmisMiRequest);
    }
}
