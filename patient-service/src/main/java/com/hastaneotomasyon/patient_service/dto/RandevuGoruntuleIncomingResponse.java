package com.hastaneotomasyon.patient_service.dto;

import java.time.LocalDateTime;

public record RandevuGoruntuleIncomingResponse(
        String doktorId,
        LocalDateTime randevuZamani,
        Long randevuId,
        String randevuKodu,
        Long hastaId,
        String randevuDurumu
) {
}
