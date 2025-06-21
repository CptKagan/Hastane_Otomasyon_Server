package com.hastaneotomasyon.patient_service.controller;


import com.hastaneotomasyon.patient_service.dto.*;
import com.hastaneotomasyon.patient_service.model.Hasta;
import com.hastaneotomasyon.patient_service.service.HastaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hasta")
@RequiredArgsConstructor
public class HastaController {

    private final HastaService hastaService;

    @PostMapping("/randevual")
    public ResponseEntity<RandevuResponse> randevuAl(@RequestBody @Valid HastaRandevuRequest hastaRandevuRequest){
        RandevuResponse randevuResponse = hastaService.randevuAl(hastaRandevuRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(randevuResponse);
    }

    // Tc kimlik içeriyor, @PathVariable tehlikeli. Hassas bilgiler pathvar olarak alınmaz, body içerisinde.
    @PostMapping("/hastagoruntule")
    public ResponseEntity<?> hastaGoruntule(@RequestBody @Valid HastaGoruntulemeRequest hastaGoruntulemeRequest){
            Hasta hasta = hastaService.hastaGoruntule(hastaGoruntulemeRequest);
            return ResponseEntity.ok(hasta);
    }

    @PostMapping("/randevugoruntule")
    public ResponseEntity<RandevuResponse> randevuGoruntule(@RequestBody RandevuGoruntuleRequest randevuGoruntuleRequest){
        RandevuResponse randevuResponse = hastaService.randevuGoruntule(randevuGoruntuleRequest);
        return ResponseEntity.ok(randevuResponse);
    }

    @PostMapping("/randevuiptal")
    public ResponseEntity<?> randevuIptal(@RequestBody RandevuGoruntuleRequest randevuGoruntuleRequest){
        hastaService.randevuIptal(randevuGoruntuleRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hastaisimsoyisimal/{hastaId}")
    public ResponseEntity<String> hastaIsimSoyisimAl(@PathVariable Long hastaId){
        String isimSoyisim = hastaService.hastaIsimSoyisimAl(hastaId);
        return ResponseEntity.ok(isimSoyisim);
    }
}
