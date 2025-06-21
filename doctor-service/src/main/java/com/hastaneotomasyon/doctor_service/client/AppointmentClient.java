package com.hastaneotomasyon.doctor_service.client;

import com.hastaneotomasyon.doctor_service.dto.NotRequest;
import com.hastaneotomasyon.doctor_service.dto.RandevuResponseHastaId;
import com.hastaneotomasyon.doctor_service.dto.TestRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface AppointmentClient {
    Logger log = LogManager.getLogger(AppointmentClient.class);

    @GetExchange("/api/randevu/gunlukrandevugoruntule/{doktorId}")
    List<RandevuResponseHastaId> gunlukRandevuGoruntule(@PathVariable String doktorId);

    @GetExchange("/api/randevu/randevugoruntule/doktor/kod/{randevuKodu}")
    RandevuResponseHastaId randevuGoruntuleKod(@PathVariable String randevuKodu,@RequestParam String doktorId);

    @PutExchange("/api/randevu/randevubaslangicibildir/{randevuId}")
    RandevuResponseHastaId randevuBaslangiciBildir(@PathVariable Long randevuId, @RequestParam String doktorId);

    @GetExchange("/api/randevu/randevugoruntule/doktor/id/{randevuId}")
    RandevuResponseHastaId randevuGoruntuleId(@PathVariable Long randevuId, @RequestParam String doktorId);

    @PatchExchange("/api/randevu/randevupublicnotekleme/{randevuId}")
    RandevuResponseHastaId randevuyaPublicNotEkleOrGuncelle(@RequestBody NotRequest notRequest, @PathVariable Long randevuId, @RequestParam String doktorId);

    @PatchExchange("/api/randevu/randevuprivatenotekleme/{randevuId}")
    RandevuResponseHastaId randevuyaPrivateNotEkleOrGuncelle(@RequestBody NotRequest notRequest, @PathVariable Long randevuId, @RequestParam String doktorId);

    @PatchExchange("/api/randevu/randevutestekleme/{randevuId}")
    RandevuResponseHastaId randevuTestEkleme(@PathVariable Long randevuId, @RequestBody TestRequest testRequest, @RequestParam String doktorId);
}