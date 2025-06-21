package com.hastaneotomasyon.doctor_service.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface PatientClient {
    Logger log = LogManager.getLogger(AppointmentClient.class);

    @GetExchange("/api/hasta/hastaisimsoyisimal/{hastaId}")
    String hastaIsimSoyisimAl(@PathVariable Long hastaId);

}
