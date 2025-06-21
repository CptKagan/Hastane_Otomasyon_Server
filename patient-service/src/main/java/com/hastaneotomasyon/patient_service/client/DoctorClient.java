package com.hastaneotomasyon.patient_service.client;

import com.hastaneotomasyon.patient_service.dto.DoktorBilgileriResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface DoctorClient {
    Logger log = LogManager.getLogger(DoctorClient.class);

    @GetExchange("/api/doktor/fetchDoktorById/{doktorId}")
    DoktorBilgileriResponse doktorBilgileriGetir(@PathVariable String doktorId);
}
