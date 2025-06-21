package com.hastaneotomasyon.appointment_service.dto;

public record HasAccessControlRequest(
        String doktorId,
        String incomingDoktorId
) {
}
