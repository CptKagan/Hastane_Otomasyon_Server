package com.hastaneotomasyon.appointment_service.dto;

public record TumTestlerSonuclanmisMiRequest(
        Long randevuId,
        int sonuclananTestSayisi
) {
}