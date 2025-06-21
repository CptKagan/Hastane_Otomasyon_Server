package com.hastaneotomasyon.appointment_service.model;

import com.hastaneotomasyon.appointment_service.dto.RandevuRequest;
import com.hastaneotomasyon.appointment_service.enums.RandevuDurum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Randevu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hastaId;

    private String doktorId;

    private LocalDateTime tercihEdilenTarih;

    @Column(nullable = false, unique = true)
    private String randevuKodu;

    private RandevuDurum randevuDurum;

    // join table, randevu_id - test_request_id
    @ElementCollection
    @CollectionTable(name = "randevu_test_requests", joinColumns = @JoinColumn(name = "randevu_id"))
    @Column(name = "test_request_id")
    private List<Long> testRequestIds = new ArrayList<>();

    @Column(columnDefinition = "TEXT", length = 1500)
    private String publicNote = "";

    @Column(columnDefinition = "TEXT", length = 1500)
    private String privateNote = "";

    public Randevu(RandevuRequest randevuRequest){
        this.tercihEdilenTarih = randevuRequest.tercihEdilenTarih();
        this.hastaId = randevuRequest.hastaId();
        this.doktorId = randevuRequest.doktorId();
        this.randevuDurum = RandevuDurum.VAKTI_GELMEDI;
    }
}
