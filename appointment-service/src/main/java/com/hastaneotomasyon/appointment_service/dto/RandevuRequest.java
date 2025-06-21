package com.hastaneotomasyon.appointment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RandevuRequest (
        @NotNull(message = "Randevu almak istediğiniz tarihi ve saati seçmelisiniz!")
        LocalDateTime tercihEdilenTarih,

        @NotBlank(message = "Bir doktor seçiniz.")
        String doktorId,

        @NotNull
        Long hastaId
) {
}