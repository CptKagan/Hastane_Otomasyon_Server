package com.hastaneotomasyon.doctor_service.dto;

import jakarta.validation.constraints.NotBlank;

public record RandevuTeshis(
        @NotBlank
        String teshisAndRecete
) {
}
