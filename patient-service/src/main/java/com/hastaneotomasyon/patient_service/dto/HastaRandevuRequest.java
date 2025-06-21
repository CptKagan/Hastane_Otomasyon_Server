package com.hastaneotomasyon.patient_service.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HastaRandevuRequest(
        @NotBlank(message = "TC Kimlik numarası boş bırakılamaz!")
        @Size(min = 11, max = 11, message = "TC kimlik numarası 11 haneli olmalıdır!")
        String tcKimlik,

        @NotBlank(message = "İsim boş bırakılamaz!")
        String isim,

        @NotBlank(message = "Soyisim boş bırakılamaz!")
        String soyIsim,

        @NotNull(message = "Doğum tarihi boş bırakılamaz!")
        LocalDate dogumTarihi,


        @NotBlank(message = "Telefon numarası boş bırakılamaz!")
        String telefonNumarasi,

        @NotNull(message = "Randevu almak istediğiniz tarihi ve saati seçmelisiniz!")
        LocalDateTime tercihEdilenTarih,

        @Nullable
        @Email(message = "Geçerli bir email adresi giriniz!")
        String email,

        @NotBlank(message = "Bir doktor seçiniz.")
        String doktorId,

        @Nullable
        @Size(max = 255, message = "Adres en fazla 255 karakter olabilir!")
        String adres
        ) {
}
