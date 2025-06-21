package com.hastaneotomasyon.lab_service.dto;

import com.hastaneotomasyon.lab_service.enums.TestResultStatus;
import com.hastaneotomasyon.lab_service.enums.TestType;
import com.hastaneotomasyon.lab_service.model.TestResult;

import java.time.LocalDateTime;

public record TestSonucuResponse(
        Long randevuId,
        Long testId,
        String testAdi,
        String sonuc,
        LocalDateTime islemZamani,
        String labUserId,
        TestResultStatus status
) {
    public TestSonucuResponse(TestResult testResult) {
        this(
                testResult.getRandevuId(),
                testResult.getTestId(),
                TestType.fromId(testResult.getTestId()).getTestAdi(),
                testResult.getSonuc(),
                testResult.getIslemZamani(),
                testResult.getLabUserId(),
                testResult.getStatus()
                );
    }
}
