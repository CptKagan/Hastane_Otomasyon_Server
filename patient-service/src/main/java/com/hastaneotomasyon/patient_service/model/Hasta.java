package com.hastaneotomasyon.patient_service.model;

import com.hastaneotomasyon.patient_service.dto.HastaRandevuRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tcKimlik;

    private String isim;

    private String soyIsim;

    private LocalDate dogumTarihi;

    private String telefonNumarasi;

    private String email;

    @Column(columnDefinition = "TEXT", length = 500)
    private String adres;

    private LocalDateTime createdAt;

    public Hasta(HastaRandevuRequest hastaRandevuRequest){
        this.tcKimlik = hastaRandevuRequest.tcKimlik();
        this.isim = hastaRandevuRequest.isim();
        this.soyIsim = hastaRandevuRequest.soyIsim();
        this.dogumTarihi = hastaRandevuRequest.dogumTarihi();
        this.telefonNumarasi = hastaRandevuRequest.telefonNumarasi();
        this.email = hastaRandevuRequest.email();
        this.adres = hastaRandevuRequest.adres();
    }
}
