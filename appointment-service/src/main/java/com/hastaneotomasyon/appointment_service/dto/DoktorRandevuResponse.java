package com.hastaneotomasyon.appointment_service.dto;

import com.hastaneotomasyon.appointment_service.model.Randevu;

import java.time.LocalDateTime;
import java.util.List;

public record DoktorRandevuResponse(
        Long hastaId,
        String doktorId,
        Long randevuId,
        LocalDateTime tercihEdilenTarih,
        String randevuKodu,
        String randevuDurum,
        String publicNote,
        String privateNote,
        List<Long> testRequestIds
) {
    public DoktorRandevuResponse(Randevu randevu){
        this(
                randevu.getHastaId(),
                randevu.getDoktorId(),
                randevu.getId(),
                randevu.getTercihEdilenTarih(),
                randevu.getRandevuKodu(),
                randevu.getRandevuDurum().toString(),
                randevu.getPublicNote(),
                randevu.getPrivateNote(),
                randevu.getTestRequestIds()
        );
    }
}
