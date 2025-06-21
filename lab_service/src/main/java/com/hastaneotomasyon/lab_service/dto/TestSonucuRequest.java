package com.hastaneotomasyon.lab_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TestSonucuRequest (
        @NotNull
        Long randevuId,
        @NotNull
        Long testId,
        String testAdi,
        @NotBlank
        String sonuc,
        String labUserId
) {
}
