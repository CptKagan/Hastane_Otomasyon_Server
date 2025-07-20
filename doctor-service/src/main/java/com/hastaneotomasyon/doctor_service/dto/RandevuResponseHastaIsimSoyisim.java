package com.hastaneotomasyon.doctor_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RandevuResponseHastaIsimSoyisim(
        LocalDateTime tercihEdilenTarih,
        Long randevuId,
        String randevuKodu,
        String hastaAdi,
        String randevuDurum,
        String publicNote,
        String privateNote,
        List<Long> testRequestIds,
        List<TestSonucuResponse> testSonuclari,
        String teshisVeRecete
) {
    public RandevuResponseHastaIsimSoyisim(RandevuResponseHastaId randevuResponseHastaId, List<TestSonucuResponse> testSonucuResponses, String isimSoyisim){
        this(
                randevuResponseHastaId.tercihEdilenTarih(),
                randevuResponseHastaId.randevuId(),
                randevuResponseHastaId.randevuKodu(),
                isimSoyisim,
                randevuResponseHastaId.randevuDurum(),
                randevuResponseHastaId.publicNote(),
                randevuResponseHastaId.privateNote(),
                randevuResponseHastaId.testRequestIds(),
                testSonucuResponses,
                randevuResponseHastaId.teshisVeRecete()
        );
    }
}
