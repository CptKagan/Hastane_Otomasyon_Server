package com.hastaneotomasyon.doctor_service.dto;

import jakarta.validation.constraints.NotBlank;

public record MuayeneBaslangiciRequest(
        @NotBlank
        Long randevuId
) {
}
