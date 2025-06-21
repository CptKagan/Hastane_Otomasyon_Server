package com.hastaneotomasyon.patient_service.client;

import com.hastaneotomasyon.patient_service.dto.RandevuGoruntuleIncomingResponse;
import com.hastaneotomasyon.patient_service.dto.RandevuRequest;
import com.hastaneotomasyon.patient_service.dto.RandevuResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface AppointmentClient {
    Logger log = LogManager.getLogger(AppointmentClient.class);

    @PostExchange("/api/randevu/tarihkontrol")
    boolean tarihKontrol(@RequestBody RandevuRequest randevuRequest);

    @PostExchange("/api/randevu/randevual")
    RandevuResponse randevuAl(@RequestBody RandevuRequest randevuRequest);

    @GetExchange("/api/randevu/randevugoruntule/kod/{randevuKodu}")
    RandevuGoruntuleIncomingResponse randevuGoruntule(@PathVariable String randevuKodu);

    @PutExchange("/api/randevu/randevuIptal/{randevuId}")
    void randevuIptal(@PathVariable Long randevuId);
}