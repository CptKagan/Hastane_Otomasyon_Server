package com.hastaneotomasyon.appointment_service.dto;

import com.hastaneotomasyon.appointment_service.model.Randevu;

import java.time.LocalDateTime;

public record RandevuResponse(
        String doktorId,
        LocalDateTime randevuZamani,
        Long randevuId,
        String randevuKodu,
        Long hastaId,
        String randevuDurumu
) {

    public RandevuResponse(Randevu randevu){
        this(
                randevu.getDoktorId(),
                randevu.getTercihEdilenTarih(),
                randevu.getId(),
                randevu.getRandevuKodu(),
                randevu.getHastaId(),
                randevu.getRandevuDurum().toString()
        );
    }
}