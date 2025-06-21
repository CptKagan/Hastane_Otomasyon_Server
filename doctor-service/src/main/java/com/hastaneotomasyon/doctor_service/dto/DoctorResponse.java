package com.hastaneotomasyon.doctor_service.dto;

import com.hastaneotomasyon.doctor_service.model.Doktor;

public record DoctorResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        String department
) {
}
