package com.hastaneotomasyon.lab_service.client;

import com.hastaneotomasyon.lab_service.dto.GunlukTestRequest;
import com.hastaneotomasyon.lab_service.dto.TestResponse;
import com.hastaneotomasyon.lab_service.dto.TumTestlerSonuclanmisMiRequest;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface AppointmentClient {
    Logger log = LogManager.getLogger(AppointmentClient.class);

    @PostExchange("/api/randevu/gunluktestgoruntule")
    List<TestResponse> gunlukTestGoruntule(@RequestBody GunlukTestRequest gunlukTestRequest);

    @PostExchange("/api/randevu/tumtestlersonuclanmismi")
    void tumTestlerSonuclanmisMi(@RequestBody TumTestlerSonuclanmisMiRequest tumTestlerSonuclanmisMiRequest);
}
