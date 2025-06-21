package com.hastaneotomasyon.appointment_service.dto;

import java.time.LocalDateTime;

public record TestResponse(
        Long randevuId,
        String doktorId,
        Long hastaId,
        Long testId,
        LocalDateTime tercihEdilenTarih
) {
}
