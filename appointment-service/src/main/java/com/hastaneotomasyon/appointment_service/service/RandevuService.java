package com.hastaneotomasyon.appointment_service.service;

import com.hastaneotomasyon.appointment_service.dto.*;
import com.hastaneotomasyon.appointment_service.enums.RandevuDurum;
import com.hastaneotomasyon.appointment_service.model.Randevu;
import com.hastaneotomasyon.appointment_service.repository.RandevuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RandevuService {
    private final RandevuRepository randevuRepository;


    public Boolean tarihKontrol(RandevuTarihKontrolRequest randevuTarihi) {
        if(randevuTarihi.tercihEdilenTarih().isBefore(LocalDateTime.now())){
            return false;
        }

        boolean doktorUygunMu = randevuRepository
                .existsByTercihEdilenTarihAndDoktorId(randevuTarihi.tercihEdilenTarih(), randevuTarihi.doktorId());

        boolean hastaUygunMu = randevuRepository
                .existsByTercihEdilenTarihAndHastaId(randevuTarihi.tercihEdilenTarih(), randevuTarihi.hastaId());



        return !hastaUygunMu && !doktorUygunMu;
    }

    public RandevuResponse randevuAl(@Valid RandevuRequest randevuRequest) {
        Randevu randevu = new Randevu(randevuRequest);
        randevu.setRandevuKodu(UUID.randomUUID().toString());
        randevuRepository.save(randevu);
        return new RandevuResponse(randevu);
    }

    public RandevuResponse randevuGoruntuleKod(String randevuKodu) {
        Optional<Randevu> randevuOpt = randevuRepository.findByRandevuKodu(randevuKodu);
        if (randevuOpt.isEmpty()) {
            throw new NoSuchElementException("Randevu bulunamadı!");
        }
        return new RandevuResponse(randevuOpt.get());
    }

    public List<DoktorRandevuResponse> gunlukRandevuGoruntule(String doktorId) {
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = LocalDate.now().plusDays(1).atStartOfDay().minusMinutes(1);
        List<Randevu> gunlukRandevuGoruntuleListe = randevuRepository.findAllByDoktorIdAndTercihEdilenTarihBetween(doktorId, baslangic, bitis);
        if (gunlukRandevuGoruntuleListe.isEmpty()) {
            return List.of();
        }
        List<DoktorRandevuResponse> gunlukRandevuResponse = new ArrayList<>();

        for (Randevu randevu : gunlukRandevuGoruntuleListe) {
            gunlukRandevuResponse.add(new DoktorRandevuResponse(randevu));
        }

        return gunlukRandevuResponse;
    }

    public void randevuIptal(Long randevuId) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(randevuId);
        if (randevuOpt.isEmpty()) {
            throw new NoSuchElementException("Randevu Bulunamadı");
        }
        Randevu randevu = randevuOpt.get();
        if(randevu.getTercihEdilenTarih().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Geçmiş bir randevu iptal edilemez.");
        }
        if (randevu.getRandevuDurum() == RandevuDurum.VAKTI_GELMEDI || randevu.getRandevuDurum() == RandevuDurum.SURUYOR) {
            randevu.setRandevuDurum(RandevuDurum.IPTAL);
            randevuRepository.save(randevu);
            return;
        }
        throw new IllegalArgumentException("Bu randevu iptal edilemez.");
    }

    public DoktorRandevuResponse randevuBaslangiciBildir(Long randevuId, String doktorId) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(randevuId);
        if(randevuOpt.isEmpty()){
            throw new IllegalArgumentException("Randevu Bulunamadı");
        }
        Randevu randevu = randevuOpt.get();
        if(!randevu.getDoktorId().equals(doktorId)){
            throw new IllegalArgumentException("Bu randevuyu sadece randevu sahibi başlatabilir.");
        }
        switch (randevu.getRandevuDurum()){
            case IPTAL, KAPANDI, TAHLIL_BEKLENIYOR:
                throw new IllegalArgumentException("Bu randevuyu güncelleme yetkiniz bulunmamaktadır");
            case SURUYOR:
                throw new IllegalArgumentException("Bu randevu halihazırda muayene halindedir.");
            case VAKTI_GELMEDI:
                randevu.setRandevuDurum(RandevuDurum.SURUYOR);
                randevuRepository.save(randevu);
        }
        return new DoktorRandevuResponse(randevu);
    }

    public RandevuResponse randevuGoruntuleId(Long randevuId) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(randevuId);
        if(randevuOpt.isEmpty()){
            throw new IllegalArgumentException("Randevu Bulunamadı");
        }
        return new RandevuResponse((randevuOpt.get()));
    }

    public DoktorRandevuResponse randevuPublicNotEkleme(Long randevuId, NotRequest notRequest, String doktorId) {
        Randevu randevu = randevuRepository.findById(randevuId).get();
        if(randevu.getRandevuDurum() == RandevuDurum.IPTAL
                || randevu.getRandevuDurum() == RandevuDurum.KAPANDI
                || randevu.getRandevuDurum() == RandevuDurum.VAKTI_GELMEDI
                || !randevu.getDoktorId().equals(doktorId)){
            throw new IllegalArgumentException("Bu randevuyu güncelleme yetkiniz bulunmamaktadır");
        }

        String randevuPublicNot = randevu.getPublicNote();
        randevuPublicNot += notRequest.not();

        randevu.setPublicNote(randevuPublicNot);
        randevuRepository.save(randevu);
        return new DoktorRandevuResponse(randevu);
    }

    public DoktorRandevuResponse randevuPrivateNotEkleme(Long randevuId, NotRequest notRequest, String doktorId) {
        Randevu randevu = randevuRepository.findById(randevuId).get();
        if(randevu.getRandevuDurum() == RandevuDurum.IPTAL
                || randevu.getRandevuDurum() == RandevuDurum.KAPANDI
                || randevu.getRandevuDurum() == RandevuDurum.VAKTI_GELMEDI
                || !randevu.getDoktorId().equals(doktorId)){
            throw new IllegalArgumentException("Bu randevuyu güncelleme yetkiniz bulunmamaktadır");
        }

        String randevuPrivateNot = randevu.getPrivateNote();
        randevuPrivateNot += notRequest.not();

        randevu.setPrivateNote(randevuPrivateNot);
        randevuRepository.save(randevu);
        return new DoktorRandevuResponse(randevu);
    }

    public boolean hasPrivateAccess(String randevuKodu, String doktorId) {
        Optional<Randevu> randevuOpt= randevuRepository.findByRandevuKodu(randevuKodu);
        if(randevuOpt.isEmpty()){
            throw new IllegalArgumentException("Randevu bulunamadı");
        }
        Randevu randevu = randevuOpt.get();
        if(!randevu.getDoktorId().equals(doktorId)){
            return false;
        }
        return true;
    }

    public DoktorRandevuResponse randevuGoruntuleDoktorKod(String randevuKodu, String doktorId) {
        Optional<Randevu> randevuOpt = randevuRepository.findByRandevuKodu(randevuKodu);
        if(randevuOpt.isEmpty()){
            throw new NoSuchElementException("Randevu bulunamadı.");
        }
        Randevu randevu = randevuOpt.get();
        if(randevu.getDoktorId().equals(doktorId)){
            return new DoktorRandevuResponse(randevu);
        }
        else{
            randevu.setPrivateNote(null);
            return new DoktorRandevuResponse(randevu);
        }
    }

    public DoktorRandevuResponse randevuGoruntuleDoktorId(Long randevuId, String doktorId) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(randevuId);
        if(randevuOpt.isEmpty()){
            throw new NoSuchElementException("Randevu bulunamadı");
        }
        Randevu randevu = randevuOpt.get();
        if(randevu.getDoktorId().equals(doktorId)){
            return new DoktorRandevuResponse(randevu);
        }
        else{
            randevu.setPrivateNote(null);
            return new DoktorRandevuResponse(randevu);
        }
    }

    public DoktorRandevuResponse randevuTestEkleme(Long randevuId, TestRequest testRequest, String doktorId) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(randevuId);
        if(randevuOpt.isEmpty()){
            throw new NoSuchElementException("Randevu bulunamadı");
        }
        Randevu randevu = randevuOpt.get();

        // RANDEVU İSTEK ATANA MI AİT?
        if(!randevu.getDoktorId().equals(doktorId)){
            throw new IllegalArgumentException("Bu randevuyu güncelleme yetkiniz bulunmamaktadır.");
        }

        // BOŞ TEST KONTROLÜ
        if (testRequest.testIdList() == null || testRequest.testIdList().isEmpty()) {
            throw new IllegalArgumentException("En az bir test belirtilmelidir.");
        }

        if(randevu.getRandevuDurum() == RandevuDurum.VAKTI_GELMEDI){
            throw new IllegalArgumentException("İşleme alınmamış randevuya test eklenemez.");
        }

        // KAPANDI / İPTAL => EKLENEMEZ
        if(randevu.getRandevuDurum() == RandevuDurum.KAPANDI || randevu.getRandevuDurum() == RandevuDurum.IPTAL){
            throw new IllegalArgumentException("İptal edilmiş / kapanmış randevuya test eklenemez.");
        }

        boolean eklendi = false;

        // TAHLİL ZATEN EKLİYSE => EKLENEMEZ
        for(Long testId : testRequest.testIdList()){
            if(!randevu.getTestRequestIds().contains(testId)){
                randevu.getTestRequestIds().add(testId);
                eklendi = true;
            }
        }

        if(eklendi){
            randevu.setRandevuDurum(RandevuDurum.TAHLIL_BEKLENIYOR);
            randevuRepository.save(randevu);
        }

        return new DoktorRandevuResponse(randevu);
    }

    public void tumTestlerSonuclanmisMi(TumTestlerSonuclanmisMiRequest tumTestlerSonuclanmisMiRequest) {
        Optional<Randevu> randevuOpt = randevuRepository.findById(tumTestlerSonuclanmisMiRequest.randevuId());
        if(randevuOpt.isEmpty()){
            throw new NoSuchElementException("Randevu bulunamadı");
        }

        Randevu randevu = randevuOpt.get();
        int istenenTestSayisi = randevu.getTestRequestIds().size();

        if(istenenTestSayisi == tumTestlerSonuclanmisMiRequest.sonuclananTestSayisi()){
            randevu.setRandevuDurum(RandevuDurum.TESHIS);
        }

        randevuRepository.save(randevu);
    }

    public List<TestResponse> gunlukTestGoruntule(GunlukTestRequest gunlukTestRequest) {
        LocalDateTime baslangic = LocalDate.now().atStartOfDay();
        LocalDateTime bitis = baslangic.plusDays(1).minusMinutes(1);
        List<Randevu> randevuList = randevuRepository.findAllByTercihEdilenTarihBetween(baslangic, bitis);

        if (randevuList.isEmpty()){
            return List.of();
        }

        List<TestResponse> testResponses = new ArrayList<>();

        for(Randevu randevu : randevuList){
            if(randevu.getRandevuDurum() != RandevuDurum.TAHLIL_BEKLENIYOR){
                continue;
            }
            for(Long testId : randevu.getTestRequestIds()){
                if(gunlukTestRequest.izinliTestlerId().contains(testId)){
                    testResponses.add(new TestResponse(
                       randevu.getId(),
                       randevu.getDoktorId(),
                       randevu.getHastaId(),
                       testId,
                       randevu.getTercihEdilenTarih()
                    ));
                }
            }
        }

        return testResponses;
    }
}
