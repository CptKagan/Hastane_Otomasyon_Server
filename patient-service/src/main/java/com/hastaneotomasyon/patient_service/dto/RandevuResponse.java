package com.hastaneotomasyon.patient_service.dto;

import java.time.LocalDateTime;

public record RandevuResponse(
        String isim,
        String soyIsim,
        Long randevuId,
        String doktorAdi,
        LocalDateTime randevuZamani,
        String randevuKodu,
        String randevuDurumu

) {}
