package com.hastaneotomasyon.patient_service.service;


import com.hastaneotomasyon.patient_service.client.AppointmentClient;
import com.hastaneotomasyon.patient_service.client.DoctorClient;
import com.hastaneotomasyon.patient_service.dto.*;
import com.hastaneotomasyon.patient_service.model.Hasta;
import com.hastaneotomasyon.patient_service.repository.HastaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HastaService {

    private final HastaRepository hastaRepository;
    private final AppointmentClient appointmentClient;
    private final DoctorClient doctorClient;

    public RandevuResponse randevuAl(HastaRandevuRequest hastaRandevuRequest) {

        Optional<Hasta> hastaOpt = hastaRepository.findByTcKimlik(hastaRandevuRequest.tcKimlik());
        Hasta hasta;
        if (hastaOpt.isEmpty()) {
            // Hasta kayıt
            hasta = new Hasta(hastaRandevuRequest);
            hasta.setCreatedAt(LocalDateTime.now());
            hastaRepository.save(hasta);
        } else {
            hasta = hastaOpt.get();
        }

        Long hastaId = hasta.getId();

        RandevuRequest randevuRequest = new RandevuRequest(hastaRandevuRequest, hastaId);

        boolean tarihUygunMu = appointmentClient.tarihKontrol(randevuRequest);
        if (!tarihUygunMu) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu tarihte randevu alınamaz!");
        }

        RandevuResponse randevuResponse = appointmentClient.randevuAl(randevuRequest);
        DoktorBilgileriResponse doktorBilgileriResponse = doctorClient.doktorBilgileriGetir(hastaRandevuRequest.doktorId());
        String doktorAdi = doktorBilgileriResponse.firstName() + " " + doktorBilgileriResponse.lastName();
        return new RandevuResponse(hasta.getIsim(), hasta.getSoyIsim(), randevuResponse.randevuId(), doktorAdi, randevuResponse.randevuZamani(), randevuResponse.randevuKodu(), randevuResponse.randevuDurumu());
    }

    public Hasta hastaGoruntule(@Valid HastaGoruntulemeRequest hastaGoruntulemeRequest) {
        Optional<Hasta> hastaOpt = hastaRepository.findByIsimAndSoyIsimAndTcKimlik(hastaGoruntulemeRequest.isim(), hastaGoruntulemeRequest.soyIsim(), hastaGoruntulemeRequest.tcKimlik());
        if (hastaOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hasta Bulunamadı.");
        }

        return hastaOpt.get();
    }

    public RandevuResponse randevuGoruntule(RandevuGoruntuleRequest randevuGoruntuleRequest) {
        RandevuGoruntuleIncomingResponse randevuGoruntuleIncomingResponse = appointmentClient.randevuGoruntule(randevuGoruntuleRequest.randevuKodu());
        Optional<Hasta> hastaOpt = hastaRepository.findById(randevuGoruntuleIncomingResponse.hastaId());
        if (!hastaOpt.isEmpty() && (hastaOpt.get().getTcKimlik().equals(randevuGoruntuleRequest.tcKimlik()))) {
            Hasta hasta = hastaOpt.get();
            DoktorBilgileriResponse doktorBilgileriResponse = doctorClient.doktorBilgileriGetir(randevuGoruntuleIncomingResponse.doktorId());
            String doktorAdi = doktorBilgileriResponse.firstName() + " " + doktorBilgileriResponse.lastName();
            return new RandevuResponse(hasta.getIsim()
                    , hasta.getSoyIsim()
                    , randevuGoruntuleIncomingResponse.randevuId()
                    , doktorAdi
                    , randevuGoruntuleIncomingResponse.randevuZamani()
                    , randevuGoruntuleRequest.randevuKodu()
                    , randevuGoruntuleIncomingResponse.randevuDurumu().toString());
        }
        throw new NoSuchElementException("Kayıt bulunamadı.");
    }

    public String hastaIsimSoyisimAl(Long hastaId) {
        Optional<Hasta> hastaOpt = hastaRepository.findById(hastaId);
        if (hastaOpt.isEmpty()) {
            throw new NoSuchElementException("Hasta bulunamadı");
        }
        return hastaOpt.get().getIsim() + " " + hastaOpt.get().getSoyIsim();
    }

    public void randevuIptal(RandevuGoruntuleRequest randevuGoruntuleRequest) {
        RandevuGoruntuleIncomingResponse randevuGoruntuleIncomingResponse = appointmentClient.randevuGoruntule(randevuGoruntuleRequest.randevuKodu());
        Optional<Hasta> hastaOpt = hastaRepository.findById(randevuGoruntuleIncomingResponse.hastaId());
        if (!hastaOpt.isEmpty() && (hastaOpt.get().getTcKimlik().equals(randevuGoruntuleRequest.tcKimlik()))) {
            appointmentClient.randevuIptal(randevuGoruntuleIncomingResponse.randevuId());
            return;
        }
        throw new NoSuchElementException("Randevu Bulunamadı");
    }
}
