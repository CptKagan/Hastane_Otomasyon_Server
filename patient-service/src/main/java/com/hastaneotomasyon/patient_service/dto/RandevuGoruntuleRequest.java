package com.hastaneotomasyon.patient_service.dto;

import jakarta.validation.constraints.NotBlank;

public record RandevuGoruntuleRequest(
        @NotBlank(message = "Lütfen TC kimlik numaranızı giriniz.")
        String tcKimlik,

        @NotBlank(message = "Lütfen randevu kodunuzu giriniz.")
        String randevuKodu
) {
}
