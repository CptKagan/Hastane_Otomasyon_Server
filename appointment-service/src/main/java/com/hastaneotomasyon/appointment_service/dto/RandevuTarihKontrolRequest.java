package com.hastaneotomasyon.appointment_service.dto;

import java.time.LocalDateTime;

public record RandevuTarihKontrolRequest(
        String doktorId,
        LocalDateTime tercihEdilenTarih,
        Long hastaId
) {
}
