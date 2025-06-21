package com.hastaneotomasyon.doctor_service.client;

import com.hastaneotomasyon.doctor_service.dto.TestSonucuResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface LabClient {
    Logger log = LogManager.getLogger(LabClient.class);

    @GetExchange("/api/laboratuvar/doktortestsonucugoruntule/{randevuId}")
    List<TestSonucuResponse> doktorTestSonucuGoruntule(@RequestHeader("Authorization") String bearerToken, @PathVariable Long randevuId);
}
