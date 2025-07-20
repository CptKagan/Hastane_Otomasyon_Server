package com.hastaneotomasyon.lab_service.dto;

import com.hastaneotomasyon.lab_service.enums.TestType;

import java.time.LocalDateTime;

public record TestResponse(
        Long randevuId,
        String doktorId,
        Long hastaId,
        Long testId,
        String testAdi,
        LocalDateTime tercihEdilenTarih,
        String durum
) {
}
