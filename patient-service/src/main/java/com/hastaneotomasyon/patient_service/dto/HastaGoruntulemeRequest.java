package com.hastaneotomasyon.patient_service.dto;

import jakarta.validation.constraints.NotBlank;

public record HastaGoruntulemeRequest (
        @NotBlank(message = "İsim boş bırakılamaz!")
        String isim,

        @NotBlank(message = "Soyisim boş bırakılamaz!")
        String soyIsim,

        @NotBlank(message = "TcKimlik boş bırakılamaz!")
        String tcKimlik) {
}
