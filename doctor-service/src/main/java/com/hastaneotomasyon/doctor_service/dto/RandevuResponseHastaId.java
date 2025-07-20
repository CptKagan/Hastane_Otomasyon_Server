package com.hastaneotomasyon.doctor_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RandevuResponseHastaId(
        Long hastaId,
        String doktorId,
        Long randevuId,
        LocalDateTime tercihEdilenTarih,
        String randevuKodu,
        String randevuDurum,
        String publicNote,
        String privateNote,
        List<Long> testRequestIds,
        String teshisVeRecete
) {
}
