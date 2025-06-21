package com.hastaneotomasyon.patient_service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RandevuRequest(
        @NotNull(message = "Randevu almak istediğiniz tarihi ve saati seçmelisiniz!")
        LocalDateTime tercihEdilenTarih,

        @NotNull(message = "Bir doktor seçiniz.")
        String doktorId,

        @NotNull
        Long hastaId
) {

    public RandevuRequest(HastaRandevuRequest hastaRandevuRequest, Long hastaId){
        this(
                hastaRandevuRequest.tercihEdilenTarih(),
                hastaRandevuRequest.doktorId(),
                hastaId
        );
    }
}
