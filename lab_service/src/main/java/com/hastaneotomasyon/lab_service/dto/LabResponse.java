package com.hastaneotomasyon.lab_service.dto;

public record LabResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        String department
) {
}
